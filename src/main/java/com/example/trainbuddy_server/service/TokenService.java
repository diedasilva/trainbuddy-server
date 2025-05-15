package com.example.trainbuddy_server.service;

import java.util.Optional;

import com.example.trainbuddy_server.entity.RefreshToken;

public interface TokenService {
    RefreshToken createRefreshToken(Long userId);
    Optional<RefreshToken> validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    void revokeUserTokens(Long userId);
    void revokeAccessTokenJti(String jti);
}
