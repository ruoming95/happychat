package com.happychat.utils;

import com.happychat.constants.Constants;
import com.happychat.enums.UserContactTypeEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class StringTools {
    public static String upperCaseFirstLetter(String str) {
        if (str == null || str == "") {
            return str;
        }
//        如果第二个字母大写，第一个字母不大写
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1))) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isEmpty(String str) {
        if (null == str || str.equals("") || "null".equals(str) || str.equals("\u0000")) {
            return true;
        } else if (str.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static String getUserId() {
        return UserContactTypeEnum.USER.getPrefix() + getRandomNumber(Constants.COUNT_11);
    }

    public static String getGroupId() {
        return UserContactTypeEnum.GROUP.getPrefix() + getRandomNumber(Constants.COUNT_11);
    }

    public static String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static String getRandomString(Integer count) {
        return RandomStringUtils.random(count, true, false);
    }

    public static String MD5HEX(String password) {
        return DigestUtils.md5Hex(password);
    }

    public static String getSessionId4User(String[] ids) {
        Arrays.sort(ids);
        String join = StringUtils.join(ids, "");
        return MD5HEX(join);
    }

    public static String getSessionId4Group(String groupId) {
        return MD5HEX(groupId);
    }

    public static String cleanHtmlTag(String content) {
        if (isEmpty(content)) {
            return content;
        }
        content = content.replace("<", "&lt;");
        content = content.replace("\r\n", "<br>");
        content = content.replace("\n", "<br>");

        return content;
    }

    public static String getFileSuffix(String fileName) {
        if (StringTools.isEmpty(fileName)) {
            return null;
        }
//        获取文件后缀
        if (fileName.lastIndexOf(".") == -1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //    用正则表达式判断是否是数字
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        String regex = "^[0-9]+$";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }
}

