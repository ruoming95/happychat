package com.happychat.enums;

public enum ResponseCodeEnum {
    CODE_200(200, "成功"),
    CODE_400(400, "失败"),
    CODE_401(401, "未授权"),
    CODE_403(403, "禁止访问"),
    CODE_404(404, "未找到"),
    CODE_413(413, "文件上传失败，大小超出限制"),
    CODE_500(500, "服务器错误，请联系管理员"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "主键冲突"),
    CODE_602(602, "文件不存在"),
    CODE_901(901, "登录超时"),
    CODE_902(902, "请先加对方为好友"),
    CODE_903(903, "请先加入群聊");

    private Integer code;
    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

