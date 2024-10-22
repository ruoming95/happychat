package com.happychat.constants;

import com.happychat.enums.UserContactTypeEnum;

public class Constants {
    public static final Integer COUNT_6 = 6;
    public static final Integer COUNT_11 = 11;
    public static final Integer COUNT_20 = 20;
    //    设置超时时间5分钟
    public static final Integer EXPIRE_TIME_1MIN = 60;
    public static final Integer EXPIRE_TIME_1DAY = 60 * 60 * 24;
    public static final Long EXPIRE_TIME_7DAY = Long.valueOf(7 * 24 * 60 * 60 * 1000);

    public static final String ROBOT_ID = UserContactTypeEnum.USER.getPrefix() + "robot";

    //Redis相关配置
    public static final String REDIS_KEY_PREFIX = "happychat:checkcode:";
    public static final String REDIS_WS_USER_HEART_BEAT = "happychat:ws:user:heartbeat:";
    public static final Integer REDIS_HERT_BEAT_EXPIRE_TIME = 6;
    public static final String REDIS_WS_USER_TOKEN = "happychat:ws:token:";
    public static final String REDIS_WS_USER_TOKEN_USERID = "happychat:ws:token:userId";
    public static final String REDIS_USER_SYSSETTING = "happychat:user:syssetting:";
    public static final String REDIS_WS_USER_CONTACT = "happychat:ws:user:contact:";

    //    文件相关配置
    public static final String FILE_FOLDER_FILE = "/file/";
    public static final String FILE_FOLDER_AVATAR = "avatar/";
    public static final String IMG_SUFFIX = ".png";
    public static final String COVER_IMG_SUFFIX = "_cover.png";
    public static final String MP3_SUFFIX = ".mp3";
    public static final String APP_UPDATE_FILE = "/app/";
    public static final String APP_SUFFIX = ".exe";
    public static final String APP_Name = "happychatSetup.";

    public static final String DEFAULT_APPLY_INFO = "我是%s";
    public static final String REGIX_PASSWORD = "^[\\w!@#$%^&*()_+-=\\[\\]{};:',<.>\\/?]{6,16}$";

    public static final String[] IMAGE_SUFFIX_SLIT = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
    public static final String[] VEDIO_SUFFIX_SLIT = new String[]{".mp4", ".avi", ".wmv", ".flv", ".mov", ".mp3", ".wav", ".wma", ".flac", ".ogg"};
    public static final String[] AUDIO_SUFFIX_SLIT = new String[]{".mp3", ".wav", ".wma", ".flac", ".ogg"};
    public static final Long ONE_MB = 1024 * 1024L;
}
