package com.happychat.exception;

import com.happychat.enums.ResponseCodeEnum;

public class BusinessException extends RuntimeException {
    private ResponseCodeEnum codeEnum;

    private Integer code;

    private String message;

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public ResponseCodeEnum getCodeEnum() {
        return codeEnum;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
