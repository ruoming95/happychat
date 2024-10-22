package com.happychat.entity.dto;

import com.happychat.entity.pojo.ChatMessage;
import com.happychat.entity.pojo.ChatSessionUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


public class InitRecordData implements Serializable {
    private List<ChatMessage> chatMessageList;

    private List<ChatSessionUser> chatSessionList;

    private Integer applyCount;


    public InitRecordData() {
    }

    public InitRecordData(List<ChatMessage> chatMessageList, List<ChatSessionUser> chatSessionList, Integer applyCount) {
        this.chatMessageList = chatMessageList;
        this.chatSessionList = chatSessionList;
        this.applyCount = applyCount;
    }

    /**
     * 获取
     * @return chatMessageList
     */
    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    /**
     * 设置
     * @param chatMessageList
     */
    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    /**
     * 获取
     * @return chatSessionList
     */
    public List<ChatSessionUser> getChatSessionList() {
        return chatSessionList;
    }

    /**
     * 设置
     * @param chatSessionList
     */
    public void setChatSessionList(List<ChatSessionUser> chatSessionList) {
        this.chatSessionList = chatSessionList;
    }

    /**
     * 获取
     * @return applyCount
     */
    public Integer getApplyCount() {
        return applyCount;
    }

    /**
     * 设置
     * @param applyCount
     */
    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public String toString() {
        return "InitRecordData{chatMessageList = " + chatMessageList + ", chatSessionList = " + chatSessionList + ", applyCount = " + applyCount + "}";
    }
}
