package com.happychat.entity.vo;

import java.io.Serializable;
import java.util.List;

public class AppUpdateVo implements Serializable {
    private Integer id;
    private String version; // 版本号
    private List<String> updateList; // 更新描述
    private Long size;
    private String fileName;
    private Integer fileType;
    private String outerLink; // 外链地址

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getOuterLink() {
        return outerLink;
    }

    public void setOuterLink(String outerLink) {
        this.outerLink = outerLink;
    }
}
