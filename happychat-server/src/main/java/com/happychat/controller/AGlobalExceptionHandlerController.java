package com.happychat.controller;

import com.happychat.entity.vo.ResponseVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseVo handleException(Exception e, HttpServletRequest request) {
        ResponseVo ajaxResponse = new ResponseVo();
        e.printStackTrace();

        // 404 异常处理
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setData(ResponseCodeEnum.CODE_404.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        }
        // 业务错误处理
        else if (e instanceof BusinessException) {
            BusinessException biz = (BusinessException) e;
            ajaxResponse.setCode(biz.getCode());
            ajaxResponse.setMsg(biz.getMessage());
            ajaxResponse.setStatus(STATIC_ERROR);
        }
        // 参数类型错误处理
        else if (e instanceof BindException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setData(ResponseCodeEnum.CODE_600.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        }
        // 主键冲突处理
        else if (e instanceof DuplicateKeyException) { // 注意：DuplicateReyException 可能是 DuplicateKeyException 的拼写错误
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
            ajaxResponse.setData(ResponseCodeEnum.CODE_601.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        }
        // 其他错误处理
        else {
            logger.error(e.getMessage());
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setMsg(ResponseCodeEnum.CODE_500.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        }

        return ajaxResponse;
    }
}
