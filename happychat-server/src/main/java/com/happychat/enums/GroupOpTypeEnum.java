package com.happychat.enums;

public enum GroupOpTypeEnum {
    EXIT(0, "退出群聊"),
    JOIN(1, "加入群聊");

    private final Integer type;
    private final String desc;

    GroupOpTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static GroupOpTypeEnum getByType(Integer type) {
        for (GroupOpTypeEnum groupOpTypeEnum : GroupOpTypeEnum.values()) {
            if (type.equals(groupOpTypeEnum.getType())) {
                return groupOpTypeEnum;
            }
        }
        return null;
    }
}
