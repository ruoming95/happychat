package com.happychat.entity.query;

import java.util.Date;

/**
 * @Description:联系人查询
 * @author:某某某
 * @date:2024/08/19
 */
public class UserContactQuery extends BaseQuery {

    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 联系人ID或者群组ID
     */
    private String contactId;

    private String contactIdFuzzy;

    /**
     * 联系人类型: 1好友 2群组
     */
    private Integer contactType;

    /**
     * 创建时间
     */
    private Date createTime;

    private String createTimeStart;

    private String createTimeEnd;

    /**
     * 状态: 0非好友 1好友 2已删除 3被好友删除 4已拉黑好友 5被好友拉黑
     */
    private Integer status;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    private String lastUpdateTimeStart;

    private String lastUpdateTimeEnd;

    private Boolean queryUserInfo;
    private Boolean queryUserContactInfo;
    private Boolean queryGroupInfo;
    private Boolean excludeMyGroup;
    private Integer[] statusArrays;

    public Integer[] getStatusArrays() {
        return statusArrays;
    }

    public void setStatusArrays(Integer[] statusArrays) {
        this.statusArrays = statusArrays;
    }

    public Boolean getExcludeMyGroup() {
        return excludeMyGroup;
    }

    public void setExcludeMyGroup(Boolean excludeMyGroup) {
        this.excludeMyGroup = excludeMyGroup;
    }

    public Boolean getQueryUserInfo() {
		return queryUserInfo;
	}

	public void setQueryUserInfo(Boolean queryUserInfo) {
		this.queryUserInfo = queryUserInfo;
	}

    public Boolean getQueryUserContactInfo() {
        return queryUserContactInfo;
    }

    public void setQueryUserContactInfo(Boolean queryUserContactInfo) {
        this.queryUserContactInfo = queryUserContactInfo;
    }

    public Boolean getQueryGroupInfo() {
        return queryGroupInfo;
    }

    public void setQueryGroupInfo(Boolean queryGroupInfo) {
        this.queryGroupInfo = queryGroupInfo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getContactType() {
        return this.contactType;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setUserIdFuzzy(String userIdFuzzy) {
        this.userIdFuzzy = userIdFuzzy;
    }

    public String getUserIdFuzzy() {
        return this.userIdFuzzy;
    }

    public void setContactIdFuzzy(String contactIdFuzzy) {
        this.contactIdFuzzy = contactIdFuzzy;
    }

    public String getContactIdFuzzy() {
        return this.contactIdFuzzy;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeStart() {
        return this.createTimeStart;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }

    public String getLastUpdateTimeStart() {
        return this.lastUpdateTimeStart;
    }

    public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public String getLastUpdateTimeEnd() {
        return this.lastUpdateTimeEnd;
    }

}