package com.happychat.service.impl;

import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.*;
import com.happychat.entity.query.*;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.*;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.*;
import com.happychat.service.GroupInfoService;
import com.happychat.service.UserContactApplyService;
import com.happychat.service.UserContactService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import com.happychat.websocket.ChannelContextUtils;
import com.happychat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description:ServiceImpl
 * @author:某某某
 * @date:2024/08/19
 */
@Service("GroupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService {

    @Autowired
    private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

    @Autowired
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Autowired
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

    @Autowired
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Autowired
    private UserContactApplyService userContactApplyService;

    @Autowired
    @Lazy
    private GroupInfoService groupInfoService;

    @Autowired
    private UserContactService userContactService;

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Autowired
    private ChannelContextUtils channelContextUtils;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    public List<GroupInfo> findListByParam(GroupInfoQuery query) {
        return this.groupInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(GroupInfoQuery query) {
        return this.groupInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<GroupInfo> findListByPage(GroupInfoQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<GroupInfo> list = this.findListByParam(query);
        ResultVo<GroupInfo> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(GroupInfo bean) {
        return this.groupInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<GroupInfo> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.groupInfoMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<GroupInfo> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.groupInfoMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据GroupId查询
     */
    public GroupInfo getGroupInfoByGroupId(String groupId) {
        return this.groupInfoMapper.selectByGroupId(groupId);
    }

    /**
     * 根据GroupId更新
     */
    public Integer updateGroupInfoByGroupId(GroupInfo bean, String groupId) {
        return this.groupInfoMapper.updateByGroupId(bean, groupId);
    }

    /**
     * 根据GroupId删除
     */
    public Integer deleteGroupInfoByGroupId(String groupId) {
        return this.groupInfoMapper.deleteByGroupId(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
        Date curDate = new Date();
//        新增
        if (StringTools.isEmpty(groupInfo.getGroupId())) {
            GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
            groupInfoQuery.setGroupOwnerId(groupInfoQuery.getGroupOwnerId());
            Integer count = groupInfoMapper.selectCount(groupInfoQuery);
//            从redis中获取到系统设置
            SyssettingDto syssetting = redisComponent.getSyssetting();
            if (count >= syssetting.getMaxGroupCount()) {
                throw new BusinessException("最多可以创建" + syssetting.getMaxGroupCount() + "个群");
            }
            if (null == avatarFile) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            String groupId = StringTools.getGroupId();
            String groupName = groupInfo.getGroupName();
            groupInfo.setGroupId(groupId);
            groupInfo.setCreateTime(curDate);
            groupInfoMapper.insert(groupInfo);

//            将群组设置为好友
            UserContact userContact = new UserContact();
            userContact.setUserId(groupInfo.getGroupOwnerId());
            userContact.setContactId(groupId);
            userContact.setContactType(UserContactStatusEnum.FRIEND.getStatus());
            userContact.setStatus(UserContactTypeEnum.GROUP.getType());
            userContact.setCreateTime(curDate);
            userContactMapper.insert(userContact);

//            创建会话 发送消息
            String sessionId = StringTools.getSessionId4Group(groupId);
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastReceiveTime(curDate.getTime());
            chatSession.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatSessionMapper.insert(chatSession);

            ChatSessionUser chatSessionUser = new ChatSessionUser();
            chatSessionUser.setUserId(groupInfo.getGroupOwnerId());
            chatSessionUser.setContactId(groupId);
            chatSessionUser.setContactName(groupName);
            chatSessionUser.setSessionId(sessionId);
            chatSessionUserMapper.insert(chatSessionUser);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSendUserId(groupId);
            chatMessage.setContactId(groupInfo.getGroupOwnerId());
            chatMessage.setSendUserNickName(groupName);
            chatMessage.setMessageType(MessageTypeEnum.GROUP_CREATE.getType());
            chatMessage.setMessageContent(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
            chatMessage.setSendTime(curDate.getTime());
            chatMessage.setSessionId(sessionId);
            chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
            chatMessageMapper.insert(chatMessage);

            redisComponent.addUserContact(groupInfo.getGroupOwnerId(), groupId);
//           降成员的连接通道添加到channelgroup里
            channelContextUtils.addUser2Group(groupInfo.getGroupOwnerId(), groupId);
//            发送ws消息
            chatSessionUser.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatSessionUser.setLastReceiveTime(curDate.getTime());
            chatSessionUser.setMemberCount(1);

            MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
            sendDto.setLastMessage(chatSessionUser.getLastMessage());
            sendDto.setExtendData(chatSessionUser);
//
            messageHandler.sendMessage(sendDto);
        } else {
            GroupInfo dbGroup = this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
            if (!dbGroup.getGroupOwnerId().equals(groupInfo.getGroupOwnerId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            groupInfoMapper.updateByGroupId(groupInfo, groupInfo.getGroupId());

            String contactName = null;
            if (!dbGroup.getGroupOwnerId().equals(groupInfo.getGroupName())) {
                contactName = groupInfo.getGroupName();
            }

            if (contactName == null) {
                return;
            }
            userContactService.updateContactName(groupInfo.getGroupId(), contactName);
        }
        if (avatarFile == null) {
            return;
        }
        String baseFileFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
        String avatarFilePath = baseFileFolder + Constants.FILE_FOLDER_AVATAR;
        File file = new File(avatarFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = file.getPath() + "/" + groupInfo.getGroupId() + Constants.IMG_SUFFIX;
        String coverFilePath = file.getPath() + "/" + groupInfo.getGroupId() + Constants.COVER_IMG_SUFFIX;
        avatarFile.transferTo(new File(filePath));
        avatarCover.transferTo(new File(coverFilePath));
    }

    @Override
    public void dissolutionGroup(String groupOwnerId, String groupId) {
        GroupInfo dbInfo = groupInfoMapper.selectByGroupId(groupId);
        if (dbInfo == null || !dbInfo.getGroupOwnerId().equals(groupOwnerId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setStatus(GroupStatusEnum.DISSOLUTION.getStatus());
        groupInfoMapper.updateByGroupId(groupInfo, groupId);

//        更新联系表
        UserContactQuery query = new UserContactQuery();
        query.setContactId(groupId);
        query.setContactType(UserContactTypeEnum.GROUP.getType());

        UserContact contact = new UserContact();
        contact.setStatus(UserContactStatusEnum.DEL.getStatus());

        userContactMapper.updateByParam(contact, query);

        List<UserContact> userContactList = userContactMapper.selectList(query);
        Date curDate = new Date();
        String messageContent = MessageTypeEnum.DISSOLUTION_GROUP.getInitMessage();
        String sessionId = StringTools.getSessionId4Group(groupId);

        ChatSession chatSession = new ChatSession();
        chatSession.setLastReceiveTime(curDate.getTime());
        chatSession.setLastMessage(messageContent);
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        for (UserContact userContact : userContactList) {
            redisComponent.removeUserContact(userContact.getUserId(), groupId);
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContactId(groupId);
        chatMessage.setSessionId(sessionId);
        chatMessage.setSendUserNickName(dbInfo.getGroupName());
        chatMessage.setMessageType(MessageTypeEnum.DISSOLUTION_GROUP.getType());
        chatMessage.setMessageContent(messageContent);
        chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
        chatMessage.setSendTime(curDate.getTime());
        chatMessageMapper.insert(chatMessage);

        MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        messageHandler.sendMessage(sendDto);
    }

    @Override
    public void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfo, String groupId, String selectContacts, Integer opType) {
        GroupInfo dbGroup = groupInfoMapper.selectByGroupId(groupId);
        if (dbGroup == null || !dbGroup.getGroupOwnerId().equals(tokenUserInfo.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String[] contactIds = selectContacts.split(",");
        for (String contactId : contactIds) {
            if (opType.equals(GroupOpTypeEnum.EXIT.getType())) {
                groupInfoService.leaveGroup(tokenUserInfo.getUserId(), groupId, MessageTypeEnum.REMOVE_GROUP);
            } else {
                userContactApplyService.addContact(contactId, null, groupId, UserContactTypeEnum.GROUP.getType(), null);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum) {
        GroupInfo dbGroup = groupInfoMapper.selectByGroupId(groupId);
        if (dbGroup == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (userId.equals(dbGroup.getGroupOwnerId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Integer count = userContactMapper.deleteByUserIdAndContactId(userId, groupId);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);

        Date curDate = new Date();
        String sessionId = StringTools.getSessionId4Group(groupId);
        String messageContent = String.format(messageTypeEnum.getInitMessage(), userInfo.getNickName());

        ChatSession chatSession = new ChatSession();
        chatSession.setLastMessage(messageContent);
        chatSession.setLastReceiveTime(curDate.getTime());
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContactId(groupId);
        chatMessage.setSendUserNickName(dbGroup.getGroupName());
        chatMessage.setMessageType(MessageTypeEnum.LEAVE_GROUP.getType());
        chatMessage.setMessageContent(messageContent);
        chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
        chatMessage.setSendTime(curDate.getTime());
        chatMessageMapper.insert(chatMessage);

        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        Integer memberCount = userContactMapper.selectCount(userContactQuery);
        MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        sendDto.setExtendData(userId);
        sendDto.setMemberCount(memberCount);

        messageHandler.sendMessage(sendDto);
    }
}

