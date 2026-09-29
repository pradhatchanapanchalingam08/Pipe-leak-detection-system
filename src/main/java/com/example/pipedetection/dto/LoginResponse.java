package com.example.pipedetection.dto;

public class LoginResponse {
    private String username;
    private String displayName;
    private String role;
    private Long customerId;

    public LoginResponse(String username, String displayName, String role, Long customerId) {
        this.username = username;
        this.displayName = displayName;
        this.role = role;
        this.customerId = customerId;
    }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getRole() { return role; }
    public Long getCustomerId() { return customerId; }
}
