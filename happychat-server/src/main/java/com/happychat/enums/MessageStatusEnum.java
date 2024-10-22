package com.happychat.enums;

public enum MessageStatusEnum {
    // 枚举实例，假设省略的部分是其他状态
    SENDING(0, "发送中"),
    SENDED(1, "已发送");

    private Integer status;
    private String desc;

    // 构造函数
    MessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    // 通过状态码获取枚举实例
    public static MessageStatusEnum getByStatus(Integer status) {
        for (MessageStatusEnum item : MessageStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item; // 这里缺少了返回语句的结束分号
            }
        }
        return null; // 如果没有找到匹配的状态码，返回null
    }

    // 获取状态码
    public Integer getStatus() {
        return status;
    }

    // 获取描述
    public String getDesc() {
        return desc;
    }

    // 设置描述
    public void setDesc(String desc) {
        this.desc = desc;
    }
}