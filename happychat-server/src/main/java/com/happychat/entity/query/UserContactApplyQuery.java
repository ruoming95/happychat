package com.happychat.entity.query;

/**
 * @Description:联系人申请查询
 * @author:某某某
 * @date:2024/08/19
 */
public class UserContactApplyQuery extends BaseQuery {

	/**
	 * 自增ID
	 */
	private Integer applyId;

	/**
	 * 申请人id
	 */
	private String applyUserId;

	private String applyUserIdFuzzy;

	/**
	 * 接收人ID
	 */
	private String receiveUserId;

	private String receiveUserIdFuzzy;

	/**
	 * 联系人类型: 0=好友, 1=群组
	 */
	private Integer contactType;

	/**
	 * 联系人群组ID
	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
	 * 最后申请时间
	 */
	private Long lastApplyTime;

	/**
	 * 状态: 0=待处理, 1=已同意, 2=已拒绝, 3=已拉黑
	 */
	private Integer status;

	/**
	 * 申请信息
	 */
	private String applyInfo;

	private String applyInfoFuzzy;

	private Long lastApplyTimestamp;

	public UserContactApplyQuery() {
	}

	public UserContactApplyQuery(Integer applyId, String applyUserId, String applyUserIdFuzzy, String receiveUserId, String receiveUserIdFuzzy, Integer contactType, String contactId, String contactIdFuzzy, Long lastApplyTime, Integer status, String applyInfo, String applyInfoFuzzy, Long lastApplyTimestamp, Boolean queryContact) {
		this.applyId = applyId;
		this.applyUserId = applyUserId;
		this.applyUserIdFuzzy = applyUserIdFuzzy;
		this.receiveUserId = receiveUserId;
		this.receiveUserIdFuzzy = receiveUserIdFuzzy;
		this.contactType = contactType;
		this.contactId = contactId;
		this.contactIdFuzzy = contactIdFuzzy;
		this.lastApplyTime = lastApplyTime;
		this.status = status;
		this.applyInfo = applyInfo;
		this.applyInfoFuzzy = applyInfoFuzzy;
		this.lastApplyTimestamp = lastApplyTimestamp;
		this.queryContact = queryContact;
	}


	public Boolean getQueryContact() {
		return queryContact;
	}

	public void setQueryContact(Boolean queryContact) {
		this.queryContact = queryContact;
	}

	private Boolean queryContact;

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public Integer getApplyId() {
		return this.applyId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserId() {
		return this.applyUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserId() {
		return this.receiveUserId;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactType() {
		return this.contactType;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return this.contactId;
	}

	public void setLastApplyTime(Long lastApplyTime) {
		this.lastApplyTime = lastApplyTime;
	}

	public Long getLastApplyTime() {
		return this.lastApplyTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
	}

	public String getApplyInfo() {
		return this.applyInfo;
	}

	public void setApplyUserIdFuzzy(String applyUserIdFuzzy) {
		this.applyUserIdFuzzy = applyUserIdFuzzy;
	}

	public String getApplyUserIdFuzzy() {
		return this.applyUserIdFuzzy;
	}

	public void setReceiveUserIdFuzzy(String receiveUserIdFuzzy) {
		this.receiveUserIdFuzzy = receiveUserIdFuzzy;
	}

	public String getReceiveUserIdFuzzy() {
		return this.receiveUserIdFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) {
		this.contactIdFuzzy = contactIdFuzzy;
	}

	public String getContactIdFuzzy() {
		return this.contactIdFuzzy;
	}

	public void setApplyInfoFuzzy(String applyInfoFuzzy) {
		this.applyInfoFuzzy = applyInfoFuzzy;
	}

	public String getApplyInfoFuzzy() {
		return this.applyInfoFuzzy;
	}

	/**
	 * 获取
	 * @return lastApplyTimestamp
	 */
	public Long getLastApplyTimestamp() {
		return lastApplyTimestamp;
	}

	/**
	 * 设置
	 * @param lastApplyTimestamp
	 */
	public void setLastApplyTimestamp(Long lastApplyTimestamp) {
		this.lastApplyTimestamp = lastApplyTimestamp;
	}

}