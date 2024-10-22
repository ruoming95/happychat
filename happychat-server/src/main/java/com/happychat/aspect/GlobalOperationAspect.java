package com.happychat.aspect;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.utils.RedisUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {

    @Autowired
    private RedisUtils redisUtils;

    private Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);

    @Before("@annotation(com.happychat.annotation.GlobalInterceptor)")
    public void interceptodDo(JoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor == null) {
                return;
            }

            if (interceptor.checkLogin() || interceptor.checkAdmin()) {
                checkLogin(interceptor.checkAdmin());
            }

        } catch (BusinessException e) {
            logger.error("全局拦截异常", e);
            throw e;
        } catch (Exception e) {
            logger.error("全局拦截异常", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } catch (Throwable e) {
            logger.error("全局拦截异常", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    private void checkLogin(Boolean checkAdmin) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_WS_USER_TOKEN + token);
        //2、校验令牌
        try {
            if (token == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_901);
            }
        } catch (Exception ex) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }

        if (checkAdmin && !tokenUserInfoDto.isAdmin()) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }

}
