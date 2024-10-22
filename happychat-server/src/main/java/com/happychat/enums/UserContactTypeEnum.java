package com.happychat.enums;

import com.happychat.utils.StringTools;

public enum UserContactTypeEnum {
    USER(0, "U", "用户"),
    GROUP(1, "G", "群组");

    private Integer type;
    private String prefix;
    private String desc;

    private UserContactTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }

    public static UserContactTypeEnum getByName(String name) {
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return UserContactTypeEnum.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static UserContactTypeEnum getByPrefix(String prefix) {
        try {
            if (StringTools.isEmpty(prefix)) {
                return null;
            }
            prefix = prefix.substring(0, 1).toUpperCase();
            for (UserContactTypeEnum typeEnum : UserContactTypeEnum.values()) {
                if (typeEnum.getPrefix().equals(prefix)) {
                    return typeEnum;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public String toString() {
        return this.name();
    }
}
