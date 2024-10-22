package com.happychat.controller;

import com.happychat.constants.Constants;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class ABaseController {
    protected static final String STATIC_SUCCESS = "success";

    protected static final String STATIC_ERROR = "error";

    @Autowired
    private RedisUtils redisUtils;

    protected <T> ResponseVo getSuccessResponseVo(T data) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setMsg(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(data);
        responseVo.setStatus(STATIC_SUCCESS);
        return responseVo;
    }

    protected <T> TokenUserInfoDto getTokenUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_WS_USER_TOKEN + token);
        return tokenUserInfoDto;
    }


}
