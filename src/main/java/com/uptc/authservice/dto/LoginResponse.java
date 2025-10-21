package com.uptc.authservice.dto;

public class LoginResponse {

    private String sessionId;
    private String username;
    private String message;

    public LoginResponse() {}

    public LoginResponse(String sessionId, String username, String message) {
        this.sessionId = sessionId;
        this.username = username;
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
