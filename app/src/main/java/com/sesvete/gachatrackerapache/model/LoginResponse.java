package com.sesvete.gachatrackerapache.model;

public class LoginResponse {
    private String status;
    private String message;
    private String uid;
    private String username;
    private long expires_at;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public long getExpires_at() {
        return expires_at;
    }
}
