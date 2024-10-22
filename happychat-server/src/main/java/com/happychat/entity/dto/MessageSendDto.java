package com.happychat.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.happychat.utils.StringTools;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSendDto<T> implements Serializable {

    // 消息ID
    private Long messageId;

    // 会话ID
    private String sessionId;

    // 发送人
    private String sendUserId;

    // 发送人昵称
    private String sendUserNickName;

    // 联系人ID
    private String contactId;

    // 联系人名称
    private String contactName;

    // 消息内容
    private String messageContent;

    // 最后的消息
    private String lastMessage;

    // 消息类型
    private Integer messageType;

    // 发送时间
    private Long sendTime;

    // 联系人类型
    private Integer contactType;

    // 扩展信息
    private T extendData;

    // 消息状态 0: 发送中 1: 已发送 对于文件是异步上传用状态处理
    private Integer status;

    // 文件信息
    private Long fileSize;

    private String fileName;

    private Integer fileType;

    // 群员
    private Integer memberCount;


    public MessageSendDto() {
    }

    public MessageSendDto(Long messageId, String sessionId, String sendUserId, String sendUserNickName, String contactId, String contactName, String messageContent, String lastMessage, Integer messageType, Long sendTime, Integer contactType, T extendData, Integer status, Long fileSize, String fileName, Integer fileType, Integer memberCount) {
        this.messageId = messageId;
        this.sessionId = sessionId;
        this.sendUserId = sendUserId;
        this.sendUserNickName = sendUserNickName;
        this.contactId = contactId;
        this.contactName = contactName;
        this.messageContent = messageContent;
        this.lastMessage = lastMessage;
        this.messageType = messageType;
        this.sendTime = sendTime;
        this.contactType = contactType;
        this.extendData = extendData;
        this.status = status;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileType = fileType;
        this.memberCount = memberCount;
    }

    /**
     * 获取
     *
     * @return messageId
     */
    public Long getMessageId() {
        return messageId;
    }

    /**
     * 设置
     *
     * @param messageId
     */
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取
     *
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 设置
     *
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 获取
     *
     * @return sendUserId
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置
     *
     * @param sendUserId
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取
     *
     * @return sendUserNickName
     */
    public String getSendUserNickName() {
        return sendUserNickName;
    }

    /**
     * 设置
     *
     * @param sendUserNickName
     */
    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    /**
     * 获取
     *
     * @return contactId
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * 设置
     *
     * @param contactId
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    /**
     * 获取
     *
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置
     *
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取
     *
     * @return messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置
     *
     * @param messageContent
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 获取
     *
     * @return lastMessage
     */
    public String getLastMessage() {
        if (StringTools.isEmpty(lastMessage)) {
            return messageContent;
        }
        return lastMessage;
    }

    /**
     * 设置
     *
     * @param lastMessage
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * 获取
     *
     * @return messageType
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置
     *
     * @param messageType
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取
     *
     * @return sendTime
     */
    public Long getSendTime() {
        return sendTime;
    }

    /**
     * 设置
     *
     * @param sendTime
     */
    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取
     *
     * @return contactType
     */
    public Integer getContactType() {
        return contactType;
    }

    /**
     * 设置
     *
     * @param contactType
     */
    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    /**
     * 获取
     *
     * @return extendData
     */
    public T getExtendData() {
        return extendData;
    }

    /**
     * 设置
     *
     * @param extendData
     */
    public void setExtendData(T extendData) {
        this.extendData = extendData;
    }

    /**
     * 获取
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     *
     * @return fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * 设置
     *
     * @param fileSize
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 获取
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取
     *
     * @return fileType
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * 设置
     *
     * @param fileType
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取
     *
     * @return memberCount
     */
    public Integer getMemberCount() {
        return memberCount;
    }

    /**
     * 设置
     *
     * @param memberCount
     */
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

}