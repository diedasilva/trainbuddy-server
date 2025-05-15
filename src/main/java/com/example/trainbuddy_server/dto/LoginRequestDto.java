package com.example.trainbuddy_server.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for login requests, containing user credentials and optional device identifier.
 */
public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    private String deviceId;

    // Constructors, getters and setters
    public LoginRequestDto() { }

    public LoginRequestDto(String username, String password, String deviceId) {
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
