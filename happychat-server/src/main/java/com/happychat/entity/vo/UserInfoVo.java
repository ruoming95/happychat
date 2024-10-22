package com.happychat.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;



public class UserInfoVo implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 0:直接加入,1:同意后加好友
     */
    private Integer joinType;

    /**
     * 性别 0:女 1:男
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 状态
     */
    @JsonIgnore
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 地区
     */
    private String areaName;

    /**
     * 地区编号
     */
    private String areaCode;

    /**
     * 最后离开时间
     */
    private Long lastOffTime;
    /**
     * token
     */
    private String token;
    /**
     * 是管理员吗
     */
    private boolean admin;
    /**
     * contactStatus
     */
    private Integer contactStatus;


    public UserInfoVo() {
    }

    public UserInfoVo(String userId, String email, String nickName, Integer joinType, Integer sex, String password, String personalSignature, Integer status, Date createTime, Date lastLoginTime, String areaName, String areaCode, Long lastOffTime, String token, boolean admin, Integer contactStatus) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.joinType = joinType;
        this.sex = sex;
        this.password = password;
        this.personalSignature = personalSignature;
        this.status = status;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.areaName = areaName;
        this.areaCode = areaCode;
        this.lastOffTime = lastOffTime;
        this.token = token;
        this.admin = admin;
        this.contactStatus = contactStatus;
    }

    /**
     * 获取
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取
     * @return joinType
     */
    public Integer getJoinType() {
        return joinType;
    }

    /**
     * 设置
     * @param joinType
     */
    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    /**
     * 获取
     * @return sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return personalSignature
     */
    public String getPersonalSignature() {
        return personalSignature;
    }

    /**
     * 设置
     * @param personalSignature
     */
    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    /**
     * 获取
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return lastLoginTime
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取
     * @return areaName
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置
     * @param areaName
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * 获取
     * @return areaCode
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置
     * @param areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取
     * @return lastOffTime
     */
    public Long getLastOffTime() {
        return lastOffTime;
    }

    /**
     * 设置
     * @param lastOffTime
     */
    public void setLastOffTime(Long lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    /**
     * 获取
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取
     * @return admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * 设置
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * 获取
     * @return contactStatus
     */
    public Integer getContactStatus() {
        return contactStatus;
    }

    /**
     * 设置
     * @param contactStatus
     */
    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String toString() {
        return "UserInfoVo{userId = " + userId + ", email = " + email + ", nickName = " + nickName + ", joinType = " + joinType + ", sex = " + sex + ", password = " + password + ", personalSignature = " + personalSignature + ", status = " + status + ", createTime = " + createTime + ", lastLoginTime = " + lastLoginTime + ", areaName = " + areaName + ", areaCode = " + areaCode + ", lastOffTime = " + lastOffTime + ", token = " + token + ", admin = " + admin + ", contactStatus = " + contactStatus + "}";
    }
}
