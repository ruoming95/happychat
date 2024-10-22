package com.happychat.controller;


import com.happychat.constants.Constants;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.UserInfoVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.UserInfoService;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.RedisUtils;
import com.happychat.utils.VerifyUtil;
import com.happychat.websocket.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private MessageHandler messageHandler;

    @RequestMapping("/checkCode")
    public ResponseVo checkCode() {
        //生成验证码
        Object[] objs = VerifyUtil.newBuilder()
                .setWidth(100)
                .setHeight(40)
                .setLines(5)
                .build()
                .createImage();
        String checkCode = objs[0].toString();
        BufferedImage image = (BufferedImage) objs[1];
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_PREFIX + checkCodeKey, checkCode, Constants.EXPIRE_TIME_1MIN * 5);
        logger.info("checkCodeKey:{},checkCode:{}", checkCodeKey, checkCode);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String imageBase64 = null;
        try {
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            imageBase64 = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return getSuccessResponseVo(ResponseCodeEnum.CODE_500);
        }
        Map<String, String> result = new HashMap<>();
        result.put("checkCodeKey", checkCodeKey);
        result.put("checkCode", imageBase64);
        return getSuccessResponseVo(result);
    }

    @RequestMapping("/register")
    public ResponseVo register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty @Pattern(regexp = Constants.REGIX_PASSWORD) String password,
                               @NotEmpty String nickName,
                               @NotEmpty String checkCode) {

        try {
            String code = (String) redisUtils.get(Constants.REDIS_KEY_PREFIX + checkCodeKey);
            if (!checkCode.equalsIgnoreCase(code)) {
                throw new BusinessException("验证码错误，请重新输入");
            }
            userInfoService.register(email, password, nickName);
            redisUtils.delete(Constants.REDIS_KEY_PREFIX + checkCodeKey);
        } finally {
        }
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/login")
    public ResponseVo login(@NotEmpty @Email String email,
                            @NotEmpty String password,
                            @NotEmpty String checkCode,
                            @NotEmpty String checkCodeKey) {
        try {
            String code = (String) redisUtils.get(Constants.REDIS_KEY_PREFIX + checkCodeKey);
            if (!checkCode.equalsIgnoreCase(code)) {
                throw new BusinessException("验证码错误，请重新输入");
            }
            UserInfoVo userInfoVo = userInfoService.login(email, password);
            redisUtils.delete(Constants.REDIS_KEY_PREFIX + checkCodeKey);
            return getSuccessResponseVo(userInfoVo);
        } finally {

        }
    }

    @RequestMapping("/getSysSetting")
    public ResponseVo getSysSetting() {
        return getSuccessResponseVo(redisComponent.getSyssetting());
    }

    @RequestMapping("/test")
    public ResponseVo Test() {
        MessageSendDto sendDto = new MessageSendDto();
        sendDto.setMessageContent("你好世界" + System.currentTimeMillis());
        messageHandler.sendMessage(sendDto);
        return getSuccessResponseVo(null);
    }
}
