package com.happychat.websocket;


import com.happychat.constants.Constants;
import com.happychat.entity.dto.InitRecordData;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.pojo.ChatMessage;
import com.happychat.entity.pojo.ChatSessionUser;
import com.happychat.entity.pojo.UserContactApply;
import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.query.ChatMessageQuery;
import com.happychat.entity.query.ChatSessionUserQuery;
import com.happychat.entity.query.UserContactApplyQuery;
import com.happychat.entity.query.UserInfoQuery;
import com.happychat.enums.MessageTypeEnum;
import com.happychat.enums.UserContactApplyStatusEnum;
import com.happychat.enums.UserContactTypeEnum;
import com.happychat.mapper.ChatMessageMapper;
import com.happychat.mapper.UserContactApplyMapper;
import com.happychat.mapper.UserInfoMapper;
import com.happychat.service.ChatSessionUserService;
import com.happychat.utils.JsonUtils;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChannelContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChannelContextUtils.class);

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Autowired
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

    @Autowired
    private ChatSessionUserService chatSessionUserService;

    private Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();
    private Map<String, ChannelGroup> GROUP_CHANNEL_MAP = new ConcurrentHashMap<>();

    public void addContext(String userId, Channel channel) {
        String channelId = channel.id().toString();
        AttributeKey key = null;
        if (!AttributeKey.exists(channelId)) {
            key = AttributeKey.newInstance(channelId);
        } else {
            key = AttributeKey.valueOf(channelId);
        }
        channel.attr(key).set(userId);
        List<String> contactList = redisComponent.getContactList(userId);
        for (String contactId : contactList) {
            if (UserContactTypeEnum.GROUP.equals(UserContactTypeEnum.getByPrefix(contactId))) {
                addGroup(contactId, channel);
            }
        }
        USER_CHANNEL_MAP.put(userId, channel);
        redisComponent.saveHeartBeat(userId);

//        更新用户最后登录时间
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setUserId(userId);
        userInfoMapper.updateByParam(updateInfo, userInfoQuery);

//        获取用户最后离线时间
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        Long lastOffTime = userInfo.getLastOffTime();
        Long tempOffTime = lastOffTime;
        if (tempOffTime != null && System.currentTimeMillis() - Constants.EXPIRE_TIME_7DAY > tempOffTime) {
            lastOffTime = System.currentTimeMillis() - Constants.EXPIRE_TIME_7DAY;
        }


//        查询会话信息，发送给客户端
        InitRecordData initRecordData = new InitRecordData();
        ChatSessionUserQuery query = new ChatSessionUserQuery();
        query.setUserId(userId);
        query.setOrderBy("last_receive_time desc");
        List<ChatSessionUser> chatSessionUserList = chatSessionUserService.findListByParam(query);
        initRecordData.setChatSessionList(chatSessionUserList);

        ChatMessageQuery messageQuery = new ChatMessageQuery();
//        查询我加入的所有群组和接收人是自己的消息
        List<String> groupIdList = contactList.stream().filter(item -> UserContactTypeEnum.GROUP.equals(UserContactTypeEnum.getByPrefix(item))).collect(Collectors.toList());
        groupIdList.add(userId);
        messageQuery.setContactIdList(groupIdList);
        messageQuery.setLastReceiveTime(lastOffTime);
        List<ChatMessage> messageList = chatMessageMapper.selectList(messageQuery);
        initRecordData.setChatMessageList(messageList);

        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setReceiveUserId(userId);
        applyQuery.setLastApplyTimestamp(lastOffTime);
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        Integer count = userContactApplyMapper.selectCount(applyQuery);
        initRecordData.setApplyCount(count);

//        发送消息
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setSendUserId(userId);
        messageSendDto.setContactId(userId);
        messageSendDto.setStatus(MessageTypeEnum.INIT.getType());
        messageSendDto.setExtendData(initRecordData);
        messageSendDto.setMessageType(MessageTypeEnum.INIT.getType());

        sendMsg(messageSendDto, userId);

    }

    public void sendMessage(MessageSendDto messageSendDto) {
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(messageSendDto.getContactId());
        if (type == null) {
            return;
        }
        switch (type) {
            case USER:
                send2User(messageSendDto);
                break;
            case GROUP:
                send2Group(messageSendDto);
                break;
        }
    }

    private void send2User(MessageSendDto messageSendDto) {
        String contactId = messageSendDto.getContactId();
        if (StringTools.isEmpty(contactId)) {
            return;
        }
        //获得联系人的连接通道
        Channel channel = USER_CHANNEL_MAP.get(contactId);
        if (channel == null) {
            return;
        }
        if (MessageTypeEnum.ADD_FRIEND_SELF.getType().equals(messageSendDto.getMessageType())) {
            messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
            UserInfo contactInfo = (UserInfo) messageSendDto.getExtendData();
            messageSendDto.setContactId(contactInfo.getUserId());
            messageSendDto.setContactName(contactInfo.getNickName());
            messageSendDto.setExtendData(null);
        } else {
            //对联系人而言, 他的联系人就是发送给他的人
            messageSendDto.setContactId(messageSendDto.getSendUserId());
            messageSendDto.setSendUserNickName(messageSendDto.getSendUserNickName());
        }
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));

        //强制下线
        if (MessageTypeEnum.FORCE_OFF_LINE.getType() == messageSendDto.getStatus()) {
            closeContext(contactId);
        }
    }

    private void send2Group(MessageSendDto messageSendDto) {
        String groupId = messageSendDto.getContactId();
        if (StringTools.isEmpty(groupId)) {
            return;
        }
        ChannelGroup channels = GROUP_CHANNEL_MAP.get(groupId);
        if (channels == null) {
            return;
        }
        channels.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));

        //移除群聊
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByType(messageSendDto.getMessageType());
        if (MessageTypeEnum.LEAVE_GROUP == messageTypeEnum || MessageTypeEnum.REMOVE_GROUP == messageTypeEnum) {
            String userId = (String) messageSendDto.getExtendData();
            redisComponent.removeUserContact(userId, groupId);
            Channel channel = USER_CHANNEL_MAP.get(userId);
            if (channel == null) {
                return;
            }
            channels.remove(channel);
        }
        if (MessageTypeEnum.DISSOLUTION_GROUP == messageTypeEnum) {
            GROUP_CHANNEL_MAP.remove(groupId);
            channels.close();
        }
    }


    private void sendMsg(MessageSendDto messageSendDto, String receiveId) {
        if (StringTools.isEmpty(receiveId)) {
            return;
        }
        Channel channel = USER_CHANNEL_MAP.get(receiveId);
        if (channel == null) {
            return;
        }
        messageSendDto.setContactId(messageSendDto.getSendUserId());
        messageSendDto.setSendUserNickName(messageSendDto.getSendUserNickName());
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));
    }

    public void addGroup(String groupId, Channel channel) {
        ChannelGroup userGroups = GROUP_CHANNEL_MAP.get(groupId);
        if (userGroups == null) {
            userGroups = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CHANNEL_MAP.put(groupId, userGroups);
        }
        if (channel == null) {
            return;
        }
        userGroups.add(channel);
    }

    public void addUser2Group(String userId, String groupId) {
        Channel channel = USER_CHANNEL_MAP.get(userId);
        addGroup(groupId, channel);
    }


    public void removeLastConn(Channel channel) {
        if (channel == null) {
            return;
        }
        Attribute<String> attr = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attr.get();
        if (StringTools.isEmpty(userId)) {
            return;
        }
        USER_CHANNEL_MAP.remove(userId);
        redisComponent.removeHearBeat(userId);
//        更新最后一连接的时间
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setLastOffTime(System.currentTimeMillis());
        userInfoMapper.updateByUserId(userInfo, userId);
    }

    public void closeContext(String userId) {
        if (StringTools.isEmpty(userId)) {
            return;
        }
        redisComponent.cleanTokenUserInfoDto(userId);
        Channel channel = USER_CHANNEL_MAP.get(userId);
        if (channel == null) {
            return;
        }
        channel.close();
    }
}
