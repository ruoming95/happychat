package com.happychat.enums;

import com.happychat.utils.StringTools;

public enum JoinTypeEnum {
    JOIN(0, "直接加入"),
    APPLY(1, "需要审核");
    private Integer type;
    private String desc;

    private JoinTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static JoinTypeEnum getByName(String name) {
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return JoinTypeEnum.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static JoinTypeEnum getByType(Integer type) {
        try {
            for (JoinTypeEnum typeEnum : JoinTypeEnum.values()) {
                if (typeEnum.getType().equals(type)) {
                    return typeEnum;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
