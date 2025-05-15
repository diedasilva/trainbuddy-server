package com.example.trainbuddy_server.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for refresh token rotation and logout requests.
 */
public class RefreshRequestDto {

    @NotBlank
    private String refreshToken;

    private String deviceId;

    public RefreshRequestDto() {
    }

    public RefreshRequestDto(String refreshToken, String deviceId) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
    }
    

    // getters and setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
