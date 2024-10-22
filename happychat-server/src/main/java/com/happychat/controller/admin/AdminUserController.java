package com.happychat.controller.admin;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.controller.ABaseController;
import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.query.UserInfoQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.enums.UserStatusEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController("adminUserController")
@RequestMapping("/admin")
@Validated
public class AdminUserController extends ABaseController {

    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping("/loadUser")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo loadUser(UserInfoQuery query) {
        ResultVo<UserInfo> listByPage = userInfoService.findListByPage(query);
        return getSuccessResponseVo(listByPage);
    }

    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo updateUserStatus(@NotEmpty String userId, @NotNull Integer status) {
        UserStatusEnum type = UserStatusEnum.getByStatus(status);
        if (type == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(status);
        userInfoService.updateUserInfoByUserId(userInfo, userId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/forceOffLine")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo forceOffLine(@NotEmpty String userId) {
        userInfoService.forceOffLine(userId);
        return getSuccessResponseVo(null);
    }

}
