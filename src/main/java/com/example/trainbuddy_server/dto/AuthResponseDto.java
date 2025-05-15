package com.example.trainbuddy_server.dto;

/**
 * DTO returned after authentication or token rotation,
 * containing both access and refresh tokens.
 */
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;

    // Constructors, getters and setters
    public AuthResponseDto() { }

    public AuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken  = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
