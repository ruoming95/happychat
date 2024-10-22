package com.happychat.controller;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.UserInfoVo;
import com.happychat.service.UserInfoService;
import com.happychat.utils.CopyTools;
import com.happychat.utils.StringTools;
import com.happychat.websocket.ChannelContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.IOException;

@RestController
@RequestMapping("/userInfo")
@Validated
public class UserInfoController extends ABaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ChannelContextUtils channelContextUtils;


    @RequestMapping("/getUserInfo")
    @GlobalInterceptor
    public ResponseVo getUserInfo(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(tokenUserInfo.getUserId());
        UserInfoVo userInfoVo = CopyTools.copy(userInfo, UserInfoVo.class);
        userInfoVo.setAreaName(userInfo.getAreaName());
        userInfoVo.setAdmin(tokenUserInfo.isAdmin());
        return getSuccessResponseVo(userInfoVo);
    }

    @RequestMapping("/saveUserInfo")
    @GlobalInterceptor
    public ResponseVo saveUserInfo(HttpServletRequest request,
                                   UserInfo userInfo,
                                   MultipartFile avatarFile,
                                   MultipartFile avatarCover) throws IOException {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        userInfo.setPassword(null);
        userInfo.setUserId(tokenUserInfo.getUserId());
        userInfo.setCreateTime(null);
        userInfo.setLastOffTime(null);
        userInfo.setStatus(null);
        userInfoService.updateUserInfo(userInfo, avatarFile, avatarCover);
        return getUserInfo(request);
    }

    @RequestMapping("/updatePassword")
    @GlobalInterceptor
    public ResponseVo updatePassword(HttpServletRequest request,
                                     @NotEmpty @Pattern(regexp = Constants.REGIX_PASSWORD) String password) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(StringTools.MD5HEX(password));
        userInfoService.updateUserInfoByUserId(userInfo, tokenUserInfo.getUserId());
//        TODD 强制退出
        channelContextUtils.closeContext(tokenUserInfo.getUserId());
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/logout")
    @GlobalInterceptor
    public ResponseVo logout(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        channelContextUtils.closeContext(tokenUserInfo.getUserId());
        return getSuccessResponseVo(null);
    }

}
