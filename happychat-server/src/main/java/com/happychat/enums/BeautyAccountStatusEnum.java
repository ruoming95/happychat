package com.happychat.enums;

public enum BeautyAccountStatusEnum {
    NO_USER(0, "已禁用"),
    USERD(1, "已启用");
    private Integer status;
    private String desc;

    BeautyAccountStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static BeautyAccountStatusEnum getByStatus(Integer status) {
        for (BeautyAccountStatusEnum statusEnum : BeautyAccountStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
