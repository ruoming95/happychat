package com.happychat.entity.dto;

import java.io.Serializable;

public class TokenUserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String token;
    private String nickName;
    private boolean isAdmin;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
