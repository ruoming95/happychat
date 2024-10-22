package com.happychat.entity.dto;

public class ContactInfoDto {
    private String userId;
    private String contactId;
    private String contactName;
    private Integer contactStatus;
    private String area;
    private Integer sex;
    private String contactType;


    public ContactInfoDto() {
    }

    public ContactInfoDto(String userId, String contactId, String contactName, Integer contactStatus, String area, Integer sex, String contactType) {
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactStatus = contactStatus;
        this.area = area;
        this.sex = sex;
        this.contactType = contactType;
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
     * @return contactId
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * 设置
     * @param contactId
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    /**
     * 获取
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    /**
     * 获取
     * @return area
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
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
     * @return contactType
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * 设置
     * @param contactType
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String toString() {
        return "ContactInfoDto{userId = " + userId + ", contactId = " + contactId + ", contactName = " + contactName + ", contactStatus = " + contactStatus + ", area = " + area + ", sex = " + sex + ", contactType = " + contactType + "}";
    }
}
