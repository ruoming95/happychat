package com.happychat.entity.dto;


import com.happychat.constants.Constants;

//默认系统设置
public class SyssettingDto {
    //最大群组数
    private Integer maxGroupCount = 5;
    //    群组最大人数
    private Integer maxGroupMemberCount = 500;
    //    图片大小
    private Integer maxImageSize = 50;
    //    文件大小
    private Integer maxFileSize = 1024;
    //    视频大小
    private Integer maxVideoSize = 1024;
    //    音频大小
    private Integer maxAudioSize = 1024;

    private String robobtUid = Constants.ROBOT_ID;
    private String robotNickName = "HappyChat";
    private String robotWelcome = "欢迎使用HappyChat";


    public SyssettingDto() {
    }

    public SyssettingDto(Integer maxGroupCount, Integer maxGroupMemberCount, Integer maxImageSize, Integer maxFileSize, Integer maxVideoSize, Integer maxAudioSize, String robobtUid, String robotNickName, String robotWelcome) {
        this.maxGroupCount = maxGroupCount;
        this.maxGroupMemberCount = maxGroupMemberCount;
        this.maxImageSize = maxImageSize;
        this.maxFileSize = maxFileSize;
        this.maxVideoSize = maxVideoSize;
        this.maxAudioSize = maxAudioSize;
        this.robobtUid = robobtUid;
        this.robotNickName = robotNickName;
        this.robotWelcome = robotWelcome;
    }

    /**
     * 获取
     * @return maxGroupCount
     */
    public Integer getMaxGroupCount() {
        return maxGroupCount;
    }

    /**
     * 设置
     * @param maxGroupCount
     */
    public void setMaxGroupCount(Integer maxGroupCount) {
        this.maxGroupCount = maxGroupCount;
    }

    /**
     * 获取
     * @return maxGroupMemberCount
     */
    public Integer getMaxGroupMemberCount() {
        return maxGroupMemberCount;
    }

    /**
     * 设置
     * @param maxGroupMemberCount
     */
    public void setMaxGroupMemberCount(Integer maxGroupMemberCount) {
        this.maxGroupMemberCount = maxGroupMemberCount;
    }

    /**
     * 获取
     * @return maxImageSize
     */
    public Integer getMaxImageSize() {
        return maxImageSize;
    }

    /**
     * 设置
     * @param maxImageSize
     */
    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    /**
     * 获取
     * @return maxFileSize
     */
    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * 设置
     * @param maxFileSize
     */
    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * 获取
     * @return maxVideoSize
     */
    public Integer getMaxVideoSize() {
        return maxVideoSize;
    }

    /**
     * 设置
     * @param maxVideoSize
     */
    public void setMaxVideoSize(Integer maxVideoSize) {
        this.maxVideoSize = maxVideoSize;
    }

    /**
     * 获取
     * @return maxAudioSize
     */
    public Integer getMaxAudioSize() {
        return maxAudioSize;
    }

    /**
     * 设置
     * @param maxAudioSize
     */
    public void setMaxAudioSize(Integer maxAudioSize) {
        this.maxAudioSize = maxAudioSize;
    }

    /**
     * 获取
     * @return robobtUid
     */
    public String getRobobtUid() {
        return robobtUid;
    }

    /**
     * 设置
     * @param robobtUid
     */
    public void setRobobtUid(String robobtUid) {
        this.robobtUid = robobtUid;
    }

    /**
     * 获取
     * @return robotNickName
     */
    public String getRobotNickName() {
        return robotNickName;
    }

    /**
     * 设置
     * @param robotNickName
     */
    public void setRobotNickName(String robotNickName) {
        this.robotNickName = robotNickName;
    }

    /**
     * 获取
     * @return robotWelcome
     */
    public String getRobotWelcome() {
        return robotWelcome;
    }

    /**
     * 设置
     * @param robotWelcome
     */
    public void setRobotWelcome(String robotWelcome) {
        this.robotWelcome = robotWelcome;
    }

}


