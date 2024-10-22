package com.happychat.service.impl;

import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.*;
import com.happychat.entity.query.*;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.*;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.*;
import com.happychat.service.UserContactApplyService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import com.happychat.websocket.ChannelContextUtils;
import com.happychat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:联系人申请ServiceImpl
 * @author:某某某
 * @date:2024/08/19
 */
@Service("UserContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService {

    @Autowired
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

    @Autowired
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Autowired
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

    @Autowired
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Autowired
    private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Autowired
    private ChannelContextUtils channelContextUtils;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    public List<UserContactApply> findListByParam(UserContactApplyQuery query) {
        return this.userContactApplyMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserContactApplyQuery query) {
        return this.userContactApplyMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<UserContactApply> findListByPage(UserContactApplyQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserContactApply> list = this.findListByParam(query);
        ResultVo<UserContactApply> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserContactApply bean) {
        return this.userContactApplyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserContactApply> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userContactApplyMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserContactApply> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userContactApplyMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据ApplyId查询联系人申请
     */
    public UserContactApply getUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.selectByApplyId(applyId);
    }

    /**
     * 根据ApplyId更新联系人申请
     */
    public Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
        return this.userContactApplyMapper.updateByApplyId(bean, applyId);
    }

    /**
     * 根据ApplyId删除联系人申请
     */
    public Integer deleteUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.deleteByApplyId(applyId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId查询联系人申请
     */
    public UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId更新联系人申请
     */
    public Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId删除联系人申请
     */
    public Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithApply(TokenUserInfoDto tokenUserInfoDto, Integer applyId, Integer status) {
        UserContactApplyStatusEnum type = UserContactApplyStatusEnum.getByStatus(status);
        if (type == null || UserContactApplyStatusEnum.INIT == type) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        UserContactApply applyInfo = userContactApplyMapper.selectByApplyId(applyId);
        if (null == applyInfo || !applyInfo.getReceiveUserId().equals(tokenUserInfoDto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        UserContactApply apply = new UserContactApply();
        apply.setStatus(type.getStatus());
        apply.setLastApplyTime(System.currentTimeMillis());

        UserContactApplyQuery query = new UserContactApplyQuery();
        query.setApplyId(applyId);
        query.setStatus(UserContactApplyStatusEnum.INIT.getStatus());


        Integer count = userContactApplyMapper.updateByParam(apply, query);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (UserContactApplyStatusEnum.PASS == type) {
//            添加联系人
            addContact(applyInfo.getApplyUserId(), applyInfo.getReceiveUserId(), applyInfo.getContactId(), applyInfo.getContactType(), applyInfo.getApplyInfo());
            return;
        }
        if (UserContactApplyStatusEnum.BLACKLIST == type) {
//            被拉黑 将对方添加到自己的拉黑名单中
            List<UserContact> userContactList = new ArrayList<>();
            Date curDate = new Date();
            UserContact contact = new UserContact();
            contact.setUserId(applyInfo.getApplyUserId());
            contact.setContactId(applyInfo.getContactId());
            contact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
            contact.setCreateTime(curDate);
            contact.setLastUpdateTime(curDate);
            contact.setContactType(UserContactTypeEnum.getByPrefix(applyInfo.getReceiveUserId()).getType());
            userContactList.add(contact);
            UserContact contactCopy = CopyTools.copy(contact, UserContact.class);
            contactCopy.setUserId(applyInfo.getContactId());
            contactCopy.setContactId(applyInfo.getApplyUserId());
            contactCopy.setStatus(UserContactStatusEnum.BLACKLIST.getStatus());
            contactCopy.setCreateTime(curDate);
            contactCopy.setContactType(UserContactTypeEnum.getByPrefix(applyInfo.getApplyUserId()).getType());
            userContactList.add(contactCopy);
            userContactMapper.insertOrUpdateBatch(userContactList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addContact(String applyId, String receiveId, String contactId, Integer contactType, String applyInfo) {
        if (UserContactTypeEnum.GROUP.getType().equals(contactType)) {
            UserContactQuery query = new UserContactQuery();
            query.setContactId(contactId);
            query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            Integer count = userContactMapper.selectCount(query);
            SyssettingDto syssetting = redisComponent.getSyssetting();
            if (syssetting.getMaxGroupCount() <= count) {
                throw new BusinessException("群聊人数已满");
            }
        }
//        双方都添加好友
        List<UserContact> userContactList = new ArrayList<>();
        Date curDate = new Date();
        UserContact contact = new UserContact();
        contact.setUserId(applyId);
        contact.setContactId(contactId);
        contact.setCreateTime(curDate);
        contact.setLastUpdateTime(curDate);
        contact.setContactType(contactType);
        contact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        userContactList.add(contact);
        if (UserContactTypeEnum.USER.getType().equals(contactType)) {
            UserContact copy = CopyTools.copy(contact, UserContact.class);
            copy.setUserId(contactId);
            copy.setContactId(applyId);
            userContactList.add(copy);
        }
        userContactMapper.insertOrUpdateBatch(userContactList);

//        讲联系人添加到缓存中
        if (UserContactTypeEnum.USER.getType().equals(contactType)) {
            redisComponent.addUserContact(receiveId, applyId);
        }
        redisComponent.addUserContact(applyId, contactId);

//        创建会话
        String sessionId = null;
        if (UserContactTypeEnum.GROUP.getType().equals(contactType)) {
            sessionId = StringTools.getSessionId4Group(contactId);
        } else {
            sessionId = StringTools.getSessionId4User(new String[]{applyId, contactId});
        }
        if (UserContactTypeEnum.USER.getType().equals(contactType)) {
//          创建会话
            List<ChatSessionUser> chatSessionUserList = new ArrayList<>();
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastMessage(applyInfo);
            chatSession.setLastReceiveTime(curDate.getTime());
            chatSessionMapper.insertOrUpdate(chatSession);

//            申请人会话
            ChatSessionUser applyChatSessionUser = new ChatSessionUser();
            applyChatSessionUser.setUserId(applyId);
            applyChatSessionUser.setSessionId(sessionId);
            applyChatSessionUser.setContactId(contactId);
            UserInfo contactInfo = userInfoMapper.selectByUserId(receiveId);
            applyChatSessionUser.setContactName(contactInfo.getNickName());
            chatSessionUserList.add(applyChatSessionUser);
//            接受人会话
            ChatSessionUser receiveChatSessionUser = new ChatSessionUser();
            receiveChatSessionUser.setUserId(receiveId);
            receiveChatSessionUser.setSessionId(sessionId);
            receiveChatSessionUser.setContactId(applyId);
            UserInfo applyUserInfo = userInfoMapper.selectByUserId(applyId);
            receiveChatSessionUser.setContactName(applyUserInfo.getNickName());
            chatSessionUserList.add(receiveChatSessionUser);
            chatSessionUserMapper.insertOrUpdateBatch(chatSessionUserList);

//            消息
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSessionId(sessionId);
            chatMessage.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
            chatMessage.setMessageContent(applyInfo);
            chatMessage.setSendUserId(applyId);
            chatMessage.setSendUserNickName(applyUserInfo.getNickName());
            chatMessage.setSendTime(curDate.getTime());
            chatMessage.setContactId(contactId);
            chatMessage.setContactType(contactType);
            chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
            chatMessageMapper.insert(chatMessage);

//            发送ws消息
            MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
            sendDto.setContactName(applyUserInfo.getNickName());
//            我发送给接收人
            messageHandler.sendMessage(sendDto);
//            接收人发送给我
            sendDto.setMessageType(MessageTypeEnum.ADD_FRIEND_SELF.getType());
//            根据我的id找到ws连接通道,找到后再改回来
            sendDto.setContactId(applyId);
            sendDto.setExtendData(contactInfo);
            messageHandler.sendMessage(sendDto);
        } else {
//            申请人加入群组
            ChatSessionUser chatSessionUser = new ChatSessionUser();
            chatSessionUser.setUserId(applyId);
            chatSessionUser.setContactId(contactId);
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            chatSessionUser.setContactName(groupInfo.getGroupName());
            chatSessionUser.setSessionId(sessionId);
            chatSessionUserMapper.insertOrUpdate(chatSessionUser);

//            将联系人加入群连接通道
            channelContextUtils.addUser2Group(applyId, groupInfo.getGroupId());

            UserInfo userInfo = userInfoMapper.selectByUserId(applyId);
            String joinMessage = String.format(MessageTypeEnum.ADD_GROUP.getInitMessage(), userInfo.getNickName());

            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastReceiveTime(curDate.getTime());
            chatSession.setLastMessage(joinMessage);
            chatSessionMapper.insertOrUpdate(chatSession);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageContent(joinMessage);
            chatMessage.setMessageType(MessageTypeEnum.ADD_GROUP.getType());
            chatMessage.setSessionId(sessionId);
            chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
            chatMessage.setContactId(contactId);
            chatMessage.setSendUserId(groupInfo.getGroupId());
            chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
            chatMessage.setSendTime(curDate.getTime());
            chatMessageMapper.insert(chatMessage);

            UserContactQuery query = new UserContactQuery();
            query.setContactId(contactId);
            query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            Integer count = userContactMapper.selectCount(query);

            MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
            sendDto.setMemberCount(count);
            sendDto.setLastMessage(joinMessage);
            sendDto.setContactName(groupInfo.getGroupName());

//            发送群消息
            messageHandler.sendMessage(sendDto);
        }
    }
}