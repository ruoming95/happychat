package com.happychat.service.impl;

import com.happychat.constants.Constants;
import com.happychat.entity.dto.ContactInfoDto;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.dto.UserContactSearchResultDto;
import com.happychat.entity.pojo.*;
import com.happychat.entity.query.*;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.*;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.*;
import com.happychat.service.UserContactApplyService;
import com.happychat.service.UserContactService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.RedisComponent;
import com.happychat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description:联系人ServiceImpl
 * @author:某某某
 * @date:2024/08/19
 */
@Service("UserContactService")
public class UserContactServiceImpl implements UserContactService {

    @Autowired
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Autowired
    private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

    @Autowired
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> applyMapper;

    @Autowired
    private UserContactApplyService userContactApplyService;

    @Autowired
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    public List<UserContact> findListByParam(UserContactQuery query) {
        return this.userContactMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserContactQuery query) {
        return this.userContactMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<UserContact> findListByPage(UserContactQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserContact> list = this.findListByParam(query);
        ResultVo<UserContact> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserContact bean) {
        return this.userContactMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserContact> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userContactMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserContact> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userContactMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据UserIdAndContactId查询联系人
     */
    public UserContact getUserContactByUserIdAndContactId(String userId, String contactId) {
        return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
    }

    /**
     * 根据UserIdAndContactId更新联系人
     */
    public Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId) {
        return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);
    }

    /**
     * 根据UserIdAndContactId删除联系人
     */
    public Integer deleteUserContactByUserIdAndContactId(String userId, String contactId) {
        return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer applyAdd(TokenUserInfoDto tokenUserInfo, String contactId, String applyInfo) {
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactId);
        if (null == type) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String applyId = tokenUserInfo.getUserId();
        String receiveId = "";
        Integer joinType = null;
        applyInfo = applyInfo == null ?
                String.format(Constants.DEFAULT_APPLY_INFO, tokenUserInfo.getNickName())
                : applyInfo;
        UserContact userContact = userContactMapper.selectByUserIdAndContactId(applyId, contactId);
        if (userContact != null && userContact.getStatus().equals(UserContactStatusEnum.BLACKLIST_BE)) {
            throw new BusinessException("你已被该联系人拉黑，无法发送好友申请");
        }
//        群聊
        if (UserContactTypeEnum.GROUP.equals(type)) {
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            if (groupInfo == null || groupInfo.getStatus().equals(GroupStatusEnum.DISSOLUTION)) {
                throw new BusinessException("群组不存在或已解散");
            }
            receiveId = groupInfo.getGroupOwnerId();
            joinType = groupInfo.getJoinType();
        } else {
            UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
            if (null == userInfo) {
                throw new BusinessException("联系人不存在");
            }
            receiveId = userInfo.getUserId();
            joinType = userInfo.getJoinType();
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (joinType.equals(JoinTypeEnum.JOIN.getType())) {
            userContactApplyService.addContact(applyId, receiveId, contactId, type.getType(), applyInfo);
            return joinType;
        }
        UserContactApply userContactApply = applyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyId, receiveId, contactId);
        if (null == userContactApply) {
            userContactApply = new UserContactApply();
            userContactApply.setApplyUserId(applyId);
            userContactApply.setContactId(contactId);
            userContactApply.setContactType(type.getType());
            userContactApply.setApplyInfo(applyInfo);
            userContactApply.setLastApplyTime(currentTimeMillis);
            userContactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            userContactApply.setReceiveUserId(receiveId);
            try {
                applyMapper.insert(userContactApply);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            UserContactApply contactApply = new UserContactApply();
            contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            contactApply.setLastApplyTime(currentTimeMillis);
            contactApply.setApplyInfo(applyInfo);
            applyMapper.updateByApplyId(contactApply, userContactApply.getApplyId());
        }

        if (null == userContactApply || !userContactApply.getStatus().equals(UserContactApplyStatusEnum.INIT)) {
            //发送ws消息
            MessageSendDto messageSendDto = new MessageSendDto();
            messageSendDto.setMessageType(MessageTypeEnum.CONTACT_APPLY.getType());
            messageSendDto.setContactId(receiveId);
            messageSendDto.setMessageContent(applyInfo);
            messageHandler.sendMessage(messageSendDto);
        }
        return joinType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserContactSearchResultDto search(String userId, String contactId) {
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactId);
        if (type == null) {
            return null;
        }
        UserContactSearchResultDto resultDto = new UserContactSearchResultDto();
        switch (type) {
            case USER:
                UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
                if (null == userInfo) {
                    return null;
                }
                resultDto = CopyTools.copy(userInfo, UserContactSearchResultDto.class);
                break;
            case GROUP:
                GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
                if (null == groupInfo) {
                    return null;
                }
                resultDto.setNickName(groupInfo.getGroupName());
                break;
        }
        resultDto.setContactId(contactId);
        resultDto.setContactType(type.toString());
        if (userId.equals(contactId)) {
            resultDto.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            return resultDto;
        }
        UserContact userContact = userContactMapper.selectByUserIdAndContactId(userId, contactId);
        resultDto.setStatus(userContact == null ? null : userContact.getStatus());
        return resultDto;
    }

    @Override
    public ContactInfoDto getContactInfo(TokenUserInfoDto tokenUserInfo, String contactId) {
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactId);
        if (type == null) {
            return null;
        }
        ContactInfoDto dto = new ContactInfoDto();
        UserContact userContact = userContactMapper.selectByUserIdAndContactId(tokenUserInfo.getUserId(), contactId);
        if (type.equals(UserContactTypeEnum.USER)) {
            UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
            if (userInfo == null) {
                return null;
            }
            dto.setContactName(userInfo.getNickName());
            dto.setContactId(userInfo.getUserId());
            dto.setContactStatus(userContact == null ? null : userContact.getStatus());
            dto.setSex(userInfo.getSex());
            dto.setArea(userInfo.getAreaName());
            dto.setContactType(type.toString());
            return dto;
        } else {
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            if (groupInfo == null) {
                return null;
            }
            dto.setContactName(groupInfo.getGroupName());
            dto.setContactId(groupInfo.getGroupId());
            dto.setContactStatus(userContact == null ? null : userContact.getStatus());
            dto.setContactType(type.toString());
            return dto;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserContact(String userId, String contactId, UserContactStatusEnum type) {
        UserContact contact = new UserContact();
        Date curDate = new Date();
        contact.setLastUpdateTime(curDate);
        contact.setStatus(type.getStatus());
        userContactMapper.updateByUserIdAndContactId(contact, userId, contactId);
        UserContact friendContact = new UserContact();
        friendContact.setLastUpdateTime(curDate);
        if (type.equals(UserContactStatusEnum.DEL)) {
            friendContact.setStatus(UserContactStatusEnum.DEL_BE.getStatus());
        }
        if (type.equals(UserContactStatusEnum.BLACKLIST)) {
            friendContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
        }
        userContactMapper.updateByUserIdAndContactId(friendContact, contactId, userId);

        redisComponent.removeUserContact(userId, contactId);

        redisComponent.removeUserContact(contactId, userId);
    }

    @Override
    public void updateContactName(String contactId, String contactName) {
        ChatSessionUser chatSessionUser = new ChatSessionUser();
        chatSessionUser.setContactName(contactName);
        ChatSessionUserQuery query = new ChatSessionUserQuery();
        query.setContactId(contactId);
        chatSessionUserMapper.updateByParam(chatSessionUser, query);
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactId);
        if (type == UserContactTypeEnum.GROUP) {
            MessageSendDto sendDto = new MessageSendDto();
            sendDto.setExtendData(contactName);
            sendDto.setContactId(contactId);
            sendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
            sendDto.setContactType(type.getType());
            messageHandler.sendMessage(sendDto);
        } else {
            List<String> contactList = redisComponent.getContactList(contactId);
            for (String friendId : contactList) {
                MessageSendDto sendDto = new MessageSendDto();
                sendDto.setExtendData(contactName);
                sendDto.setSendUserId(contactId);
                sendDto.setSendUserNickName(contactName);
                sendDto.setContactId(friendId);
                sendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
                sendDto.setContactType(type.getType());
                messageHandler.sendMessage(sendDto);
            }
        }
    }
}