package com.happychat.service.impl;

import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.*;
import com.happychat.entity.query.*;
import com.happychat.entity.vo.ResultVo;
import com.happychat.entity.vo.UserInfoVo;
import com.happychat.enums.*;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.*;
import com.happychat.service.UserContactService;
import com.happychat.service.UserInfoService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import com.happychat.websocket.MessageHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:用户信息ServiceImpl
 * @author:某某某
 * @date:2024/08/17
 */
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Autowired
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Autowired
    private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

    @Autowired
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

    @Autowired
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Autowired
    private UserContactService userContactService;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RedisComponent redisComponent;


    /**
     * 根据条件查询列表
     */
    public List<UserInfo> findListByParam(UserInfoQuery query) {
        return this.userInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserInfoQuery query) {
        return this.userInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<UserInfo> findListByPage(UserInfoQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(query);
        ResultVo<UserInfo> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserInfo bean) {
        return this.userInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserInfo> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userInfoMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserInfo> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userInfoMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据UserId查询用户信息
     */
    public UserInfo getUserInfoByUserId(String userId) {
        return this.userInfoMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId更新用户信息
     */
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return this.userInfoMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除用户信息
     */
    public Integer deleteUserInfoByUserId(String userId) {
        return this.userInfoMapper.deleteByUserId(userId);
    }

    /**
     * 根据Email查询用户信息
     */
    public UserInfo getUserInfoByEmail(String email) {
        return this.userInfoMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新用户信息
     */
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return this.userInfoMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除用户信息
     */
    public Integer deleteUserInfoByEmail(String email) {
        return this.userInfoMapper.deleteByEmail(email);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String password, String nickName) {
        //是否已经注册
        UserInfo userInfo = userInfoMapper.selectByEmail(email);

        if (userInfo != null) {
            throw new BusinessException("账号已经存在");
        }

        String userId = StringTools.getUserId();
        UserInfoBeauty userInfoBeauty = userInfoBeautyMapper.selectByEmail(email);
        boolean userBeautyCount = userInfoBeauty != null && BeautyAccountStatusEnum.NO_USER.getStatus().equals(userInfoBeauty.getStatus());
        if (userBeautyCount) {
            userId = UserContactTypeEnum.USER.getPrefix() + userInfoBeauty.getUserId();
        }

        Date curDate = new Date();
        userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setEmail(email);
        userInfo.setNickName(nickName);
        userInfo.setPassword(StringTools.MD5HEX(password));
        userInfo.setCreateTime(curDate);
        userInfo.setLastOffTime(curDate.getTime());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfo.setJoinType(JoinTypeEnum.JOIN.getType());
        userInfoMapper.insert(userInfo);

        if (userBeautyCount) {
            userInfoBeauty.setStatus(BeautyAccountStatusEnum.USERD.getStatus());
            userInfoBeautyMapper.updateByEmail(userInfoBeauty, email);
        }
//        注册成功， 自动添加机器人为好友
        addContactRobot(userId);
    }

    @Override
    public UserInfoVo login(String email, String password) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo == null || !password.equals(userInfo.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }

        if (userInfo.getStatus().equals(UserStatusEnum.DISABLE.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }
//        获取到心跳，判断是否登录中
        Long heartBeat = redisComponent.getHeartBeat(userInfo.getUserId());
        if (null != heartBeat) {
            throw new BusinessException("账号已在其他地方登录");
        }

        TokenUserInfoDto tokenUserInfo = getTokenUserInfoDto(userInfo);
//        查询联系人，添加到内存
        UserContactQuery query = new UserContactQuery();
        query.setUserId(tokenUserInfo.getUserId());
        query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        List<UserContact> userContacts = userContactMapper.selectList(query);
        List<String> list = userContacts.stream().map(item -> item.getContactId()).collect(Collectors.toList());
        redisComponent.cleanUpUserContact(userInfo.getUserId());
        if (!list.isEmpty()) {
            redisComponent.saveContactList(userInfo.getUserId(), list);
        }
        String token = StringTools.MD5HEX(userInfo.getUserId() + StringTools.getRandomString(Constants.COUNT_20));
        logger.info("token:{}", token);
        tokenUserInfo.setToken(token);
//  TOD 将登录信息保存到redis中
        redisComponent.saveTokenUserInfoDto(tokenUserInfo);
        UserInfoVo userInfoVo = CopyTools.copy(userInfo, UserInfoVo.class);
        userInfoVo.setToken(tokenUserInfo.getToken());
        userInfoVo.setAdmin(tokenUserInfo.isAdmin());
        return userInfoVo;
    }

    private TokenUserInfoDto getTokenUserInfoDto(UserInfo userInfo) {
        TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
        tokenUserInfoDto.setUserId(userInfo.getUserId());
        tokenUserInfoDto.setNickName(userInfo.getNickName());
        tokenUserInfoDto.setAdmin(!StringTools.isEmpty(appConfig.getAdminEmails()) && ArrayUtils.contains(appConfig.getAdminEmails().split(","), userInfo.getEmail()));
        tokenUserInfoDto.setToken("");
        return tokenUserInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
        if (avatarFile != null) {
            String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
            File avatarPath = new File(baseFolder + Constants.FILE_FOLDER_AVATAR);
            if (!avatarPath.exists()) {
                avatarPath.mkdirs();
            }
            String filePath = avatarPath + "/" + userInfo.getUserId() + Constants.IMG_SUFFIX;
            avatarFile.transferTo(new File(filePath));
            avatarCover.transferTo(new File(filePath + Constants.COVER_IMG_SUFFIX));
        }
        UserInfo dbUserInfo = userInfoMapper.selectByUserId(userInfo.getUserId());
        String nickNameUpdate = "";
        Integer count = userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());
        if (count != null && count > 0) {
            if (!dbUserInfo.getNickName().equals(userInfo.getNickName())) {
                nickNameUpdate = userInfo.getNickName();
            }
        }
        if (StringTools.isEmpty(nickNameUpdate)) {
            return;
        }
//        也要更新token信息
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByUserId(userInfo.getUserId());
        tokenUserInfoDto.setNickName(nickNameUpdate);
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

        userContactService.updateContactName(userInfo.getUserId(), nickNameUpdate);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addContactRobot(String userId) {
        Date curDate = new Date();
        SyssettingDto syssetting = redisComponent.getSyssetting();
        String robobtUid = syssetting.getRobobtUid();
        String robotNickName = syssetting.getRobotNickName();
        String robotWelcome = syssetting.getRobotWelcome();
        UserContact contact = new UserContact();
        contact.setUserId(userId);
        contact.setContactId(robobtUid);
        contact.setContactType(UserContactTypeEnum.USER.getType());
        contact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        contact.setContactName(robotNickName);
        contact.setLastUpdateTime(curDate);
        contact.setCreateTime(curDate);
        userContactMapper.insert(contact);

        //会话信息
        String sessionId = StringTools.getSessionId4User(new String[]{robobtUid, userId});
        ChatSessionUser chatSessionUser = new ChatSessionUser();
        chatSessionUser.setUserId(userId);
        chatSessionUser.setContactId(robobtUid);
        chatSessionUser.setSessionId(sessionId);
        chatSessionUser.setContactName(robotNickName);
        chatSessionUserMapper.insert(chatSessionUser);


        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setLastReceiveTime(curDate.getTime());
        chatSession.setLastMessage(robotWelcome);
        chatSessionMapper.insert(chatSession);


        //消息信息
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContactId(userId);
        chatMessage.setContactType(UserContactTypeEnum.USER.getType());
        chatMessage.setSessionId(sessionId);
        chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        chatMessage.setSendTime(curDate.getTime());
        chatMessage.setMessageContent(robotWelcome);
        chatMessage.setSendUserId(robobtUid);
        chatMessage.setSendUserNickName(robotNickName);
        chatMessage.setMessageType(MessageTypeEnum.CHAT.getType());
        chatMessageMapper.insert(chatMessage);

    }

    @Override
    public void forceOffLine(String userId) {
        if (userId == null || userId.equals(appConfig.getAdminEmails())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //获取心跳，判断是否登录
        Long heartBeat = redisComponent.getHeartBeat(Constants.REDIS_WS_USER_HEART_BEAT + userId);
        if (heartBeat == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        redisComponent.removeHearBeat(userId);
        MessageSendDto sendDto = new MessageSendDto();
        sendDto.setContactType(UserContactTypeEnum.USER.getType());
        sendDto.setMessageType(MessageTypeEnum.FORCE_OFF_LINE.getType());
        sendDto.setContactId(userId);
        messageHandler.sendMessage(sendDto);
    }
}

