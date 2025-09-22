package com.sesvete.gachatrackerapache.model;

public class LoginResponse {
    private String status;
    private String message;
    private String uid;
    private String username;
    private String token;

    public LoginResponse(String status, String message, String uid, String username, String token) {
        this.status = status;
        this.message = message;
        this.uid = uid;
        this.username = username;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
