package com.happychat.enums;

public enum UserContactApplyStatusEnum {
    INIT(0, "待处理"),
    PASS(1, "已同意"),
    REJECT(2, "已拒绝"),
    BLACKLIST(3, "已拉黑"); // 修正了原图的 "BLACRLIST" 错误拼写

    private Integer status;
    private String desc;

    UserContactApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static UserContactApplyStatusEnum getByStatus(String status) {
        try {
            if (status == null) {
                return null;
            }
            return UserContactApplyStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static UserContactApplyStatusEnum getByStatus(Integer status) {
        for (UserContactApplyStatusEnum item : UserContactApplyStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }
}