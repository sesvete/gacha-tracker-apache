package com.sesvete.gachatrackerapache.model;

public class RefreshTokenResponse {
    private String status;
    private String message;
    private long expireTime;
    private String token;


    public RefreshTokenResponse(String status, String message, long expireTime, String token) {
        this.status = status;
        this.message = message;
        this.expireTime = expireTime;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
