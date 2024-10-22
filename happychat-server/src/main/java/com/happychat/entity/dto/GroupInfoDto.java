package com.happychat.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class GroupInfoDto implements Serializable {
    /**
     * 群ID
     */
    private String groupId;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群主id
     */
    private String groupOwnerId;


    /**
     * 群公告
     */
    private String groupNotice;

    /**
     * 加入类型 0:直接加入 1:管理员同意后加入
     */
    @NotNull
    private Integer joinType;

    /**
     * 状态 1: 正常 0:解散
     */
    @JsonIgnore
    private Integer status;

    /**
     * 头像文件
     */
    private MultipartFile avatarFile;

    /**
     * 头像封面，缩略图
     */
    private MultipartFile avatarCover;


    public GroupInfoDto() {
    }

    public GroupInfoDto(String groupId, String groupName, String groupOwnerId, String groupNotice, Integer joinType, Integer status, MultipartFile avatarFile, MultipartFile avatarCover) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupOwnerId = groupOwnerId;
        this.groupNotice = groupNotice;
        this.joinType = joinType;
        this.status = status;
        this.avatarFile = avatarFile;
        this.avatarCover = avatarCover;
    }

    /**
     * 获取
     *
     * @return groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 设置
     *
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取
     *
     * @return groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置
     *
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取
     *
     * @return groupOwnerId
     */
    public String getGroupOwnerId() {
        return groupOwnerId;
    }

    /**
     * 设置
     *
     * @param groupOwnerId
     */
    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    /**
     * 获取
     *
     * @return groupNotice
     */
    public String getGroupNotice() {
        return groupNotice;
    }

    /**
     * 设置
     *
     * @param groupNotice
     */
    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    /**
     * 获取
     *
     * @return joinType
     */
    public Integer getJoinType() {
        return joinType;
    }

    /**
     * 设置
     *
     * @param joinType
     */
    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
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
     * @return avatarFile
     */
    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    /**
     * 设置
     *
     * @param avatarFile
     */
    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }

    /**
     * 获取
     *
     * @return avatarCover
     */
    public MultipartFile getAvatarCover() {
        return avatarCover;
    }

    /**
     * 设置
     *
     * @param avatarCover
     */
    public void setAvatarCover(MultipartFile avatarCover) {
        this.avatarCover = avatarCover;
    }

    public String toString() {
        return "GroupInfoDto{groupId = " + groupId + ", groupName = " + groupName + ", groupOwnerId = " + groupOwnerId + ", groupNotice = " + groupNotice + ", joinType = " + joinType + ", status = " + status + ", avatarFile = " + avatarFile + ", avatarCover = " + avatarCover + "}";
    }
}
