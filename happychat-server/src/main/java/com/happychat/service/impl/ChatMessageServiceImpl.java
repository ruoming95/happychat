package com.happychat.service.impl;

import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.ChatMessage;
import com.happychat.entity.pojo.ChatSession;
import com.happychat.entity.query.ChatMessageQuery;
import com.happychat.entity.query.ChatSessionQuery;
import com.happychat.entity.query.SimplePage;
import com.happychat.entity.query.UserContactQuery;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.*;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.ChatMessageMapper;
import com.happychat.mapper.ChatSessionMapper;
import com.happychat.mapper.UserContactMapper;
import com.happychat.service.ChatMessageService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.DateUtils;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import com.happychat.websocket.MessageHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description:聊天消息表ServiceImpl
 * @author:某某某
 * @date:2024/08/26
 */
@Service("ChatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Autowired
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

    @Autowired
    private UserContactMapper<ChatSession, ChatSessionQuery> userContactMapper;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    public List<ChatMessage> findListByParam(ChatMessageQuery query) {
        return this.chatMessageMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(ChatMessageQuery query) {
        return this.chatMessageMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<ChatMessage> findListByPage(ChatMessageQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<ChatMessage> list = this.findListByParam(query);
        ResultVo<ChatMessage> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(ChatMessage bean) {
        return this.chatMessageMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<ChatMessage> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.chatMessageMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<ChatMessage> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.chatMessageMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据MessageId查询聊天消息表
     */
    public ChatMessage getChatMessageByMessageId(Long messageId) {
        return this.chatMessageMapper.selectByMessageId(messageId);
    }

    /**
     * 根据MessageId更新聊天消息表
     */
    public Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId) {
        return this.chatMessageMapper.updateByMessageId(bean, messageId);
    }

    /**
     * 根据MessageId删除聊天消息表
     */
    public Integer deleteChatMessageByMessageId(Long messageId) {
        return this.chatMessageMapper.deleteByMessageId(messageId);
    }

    @Override
    public MessageSendDto saveChatMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto) {
        if (!Constants.ROBOT_ID.equals(tokenUserInfoDto.getUserId())) {
            List<String> contactList = redisComponent.getContactList(tokenUserInfoDto.getUserId());
            //不是联系人
            if (null != contactList && !contactList.contains(chatMessage.getContactId())) {
                if (UserContactTypeEnum.USER == UserContactTypeEnum.getByPrefix(chatMessage.getContactId())) {
                    throw new BusinessException(ResponseCodeEnum.CODE_902);
                } else {
                    throw new BusinessException(ResponseCodeEnum.CODE_903);
                }
            }
        }

        String sessionId = null;
        String contactId = chatMessage.getContactId();
        if (UserContactTypeEnum.getByPrefix(contactId).equals(UserContactTypeEnum.USER)) {
            sessionId = StringTools.getSessionId4User(new String[]{tokenUserInfoDto.getUserId(), contactId});
        } else {
            sessionId = StringTools.getSessionId4Group(contactId);
        }
        Long curDate = System.currentTimeMillis();
        String messageContent = chatMessage.getMessageContent();
        messageContent = StringTools.cleanHtmlTag(messageContent);
        chatMessage.setMessageContent(messageContent);

        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByType(chatMessage.getMessageType());
        if (null == messageTypeEnum || !ArrayUtils.contains(new Integer[]{MessageTypeEnum.CHAT.getType(),
                MessageTypeEnum.MEDIA_CHAT.getType()}, messageTypeEnum.getType())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        ChatSession chatSession = new ChatSession();
        chatSession.setLastMessage(tokenUserInfoDto.getNickName() + ": " + messageContent);
        chatSession.setLastReceiveTime(curDate);
        chatSession.setSessionId(sessionId);
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        chatMessage.setSessionId(sessionId);
        chatMessage.setSendTime(curDate);
        chatMessage.setSendUserId(tokenUserInfoDto.getUserId());
        chatMessage.setSendUserNickName(tokenUserInfoDto.getNickName());
        chatMessage.setContactType(UserContactTypeEnum.getByPrefix(contactId).getType());
        MessageStatusEnum messageStatusEnum = messageTypeEnum == MessageTypeEnum.MEDIA_CHAT ?
                MessageStatusEnum.SENDING : MessageStatusEnum.SENDED;
        chatMessage.setStatus(messageStatusEnum.getStatus());
        chatMessageMapper.insert(chatMessage);


        MessageSendDto sendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        sendDto.setMessageId(chatMessage.getMessageId());

        if (Constants.ROBOT_ID.equals(contactId)) {
            SyssettingDto syssetting = redisComponent.getSyssetting();
            TokenUserInfoDto robot = new TokenUserInfoDto();
            robot.setUserId(syssetting.getRobobtUid());
            robot.setNickName(syssetting.getRobotNickName());
            ChatMessage robotMessage = new ChatMessage();
            robotMessage.setContactId(tokenUserInfoDto.getUserId());
            robotMessage.setMessageContent("我还没有接入AI模型，暂时回复不了你的消息");
            robotMessage.setMessageType(MessageTypeEnum.CHAT.getType());
            saveChatMessage(robotMessage, robot);
        } else {
            messageHandler.sendMessage(sendDto);
        }
        return sendDto;
    }

    @Override
    public void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover) {
        ChatMessage chatMessage = chatMessageMapper.selectByMessageId(messageId);
        if (chatMessage == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!chatMessage.getSendUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SyssettingDto syssetting = redisComponent.getSyssetting();
        String fileSuffix = StringTools.getFileSuffix(file.getOriginalFilename());
        if (fileSuffix == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (ArrayUtils.contains(Constants.IMAGE_SUFFIX_SLIT, fileSuffix.toLowerCase())
                && file.getSize() > syssetting.getMaxImageSize() * Constants.ONE_MB) {
            throw new BusinessException(ResponseCodeEnum.CODE_413);
        } else if ((ArrayUtils.contains(Constants.VEDIO_SUFFIX_SLIT, fileSuffix.toLowerCase())
                && file.getSize() > syssetting.getMaxVideoSize() * Constants.ONE_MB)) {
            throw new BusinessException(ResponseCodeEnum.CODE_413);
        } else if ((ArrayUtils.contains(Constants.AUDIO_SUFFIX_SLIT, fileSuffix.toLowerCase()))
                && file.getSize() > syssetting.getMaxAudioSize() * Constants.ONE_MB) {
            throw new BusinessException(ResponseCodeEnum.CODE_413);
        } else if (!ArrayUtils.contains(Constants.VEDIO_SUFFIX_SLIT, fileSuffix.toLowerCase())
                && !ArrayUtils.contains(Constants.IMAGE_SUFFIX_SLIT, fileSuffix.toLowerCase())
                && !ArrayUtils.contains(Constants.AUDIO_SUFFIX_SLIT, fileSuffix.toLowerCase())
                && file.getSize() > syssetting.getMaxFileSize() * Constants.ONE_MB) {
            throw new BusinessException(ResponseCodeEnum.CODE_413);
        }

        String downloadSuffix = fileSuffix;
//        if (ArrayUtils.contains(Constants.AUDIO_SUFFIX_SLIT, fileSuffix.toLowerCase())) {
//            downloadSuffix = Constants.MP3_SUFFIX;
//        } else if (ArrayUtils.contains(Constants.IMAGE_SUFFIX_SLIT, fileSuffix.toLowerCase())) {
//            downloadSuffix = Constants.IMG_SUFFIX;
//        }
        String realFileName = messageId + downloadSuffix;
        String month = DateUtils.format(new Date(chatMessage.getSendTime()), DateTimePatternEnum.YYYYMM.getPattern());
        String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + month;
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        File uploadedFile = new File(fileFolder.getPath() + "/" + realFileName);
        try {
            file.transferTo(uploadedFile);
            if (cover != null) {
                cover.transferTo(new File(uploadedFile.getPath() + Constants.COVER_IMG_SUFFIX));
            }
        } catch (IOException e) {
            logger.info("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
        ChatMessage message = new ChatMessage();
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        ChatMessageQuery query = new ChatMessageQuery();
        query.setMessageId(messageId);
//        多个人同时要修改， 条件加状态就可以只让一个人修改
        query.setStatus(MessageStatusEnum.SENDING.getStatus());
        chatMessageMapper.updateByParam(message, query);

        MessageSendDto sendDto = new MessageSendDto();
        sendDto.setStatus(MessageStatusEnum.SENDED.getStatus());
        sendDto.setMessageType(MessageTypeEnum.FILE_UPLOAD.getType());
        sendDto.setContactId(chatMessage.getContactId());
        sendDto.setMessageId(messageId);

        messageHandler.sendMessage(sendDto);
    }

    @Override
    public File downloadFile(TokenUserInfoDto tokenUserInfo, Long fileId, Boolean showCover) {
        ChatMessage chatMessage = chatMessageMapper.selectByMessageId(fileId);
        if (chatMessage == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String contactId = chatMessage.getContactId();
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactId);
        if (UserContactTypeEnum.USER == type && !tokenUserInfo.getUserId().equals(contactId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (UserContactTypeEnum.GROUP == type) {
            UserContactQuery query = new UserContactQuery();
            query.setUserId(tokenUserInfo.getUserId());
            query.setContactId(contactId);
            query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            query.setContactType(type.getType());

            Integer count = userContactMapper.selectCount(query);
            if (count == 0) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }

        String month = DateUtils.format(new Date(chatMessage.getSendTime()), DateTimePatternEnum.YYYYMM.getPattern());
        String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + month;
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String fileName = chatMessage.getFileName();
        if (StringTools.isEmpty(fileName)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String fileSuffix = StringTools.getFileSuffix(fileName);
        String downloadSuffix = fileSuffix;
//        if (ArrayUtils.contains(Constants.AUDIO_SUFFIX_SLIT, fileSuffix.toLowerCase())) {
//            downloadSuffix = Constants.MP3_SUFFIX;
//        } else if (ArrayUtils.contains(Constants.IMAGE_SUFFIX_SLIT, fileSuffix.toLowerCase())) {
//            downloadSuffix = Constants.IMG_SUFFIX;
//        }
        String realFileName = fileId + downloadSuffix;
        if (showCover != null && showCover) {
            realFileName = realFileName + Constants.COVER_IMG_SUFFIX;
        }
        File file = new File(fileFolder.getPath() + "/" + realFileName);
        if (!file.exists()) {
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        return file;
    }
}