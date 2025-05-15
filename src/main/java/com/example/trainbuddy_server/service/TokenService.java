package com.example.trainbuddy_server.service;

import com.example.trainbuddy_server.entity.RefreshToken;

/**
 * Service interface for managing refresh tokens lifecycle.
 */
public interface TokenService {
    /**
     * Create and persist a new refresh token for the given user.
     *
     * @param userId   the user’s database ID
     * @param deviceId optional device identifier (may be null)
     * @return the newly created RefreshToken entity
     */
    RefreshToken createRefreshToken(Long userId, String deviceId);

    /**
     * Rotate a refresh token: invalidate the old token and issue a new one.
     *
     * @param oldToken the value of the existing refresh token
     * @return the new RefreshToken entity
     * @throws IllegalArgumentException if the old token is invalid or already revoked
     */
    RefreshToken rotateRefreshToken(String oldToken);

    /**
     * Revoke a single refresh token by marking it revoked.
     *
     * @param token the value of the refresh token to revoke
     */
    void revokeRefreshToken(String token);

    /**
     * Revoke all refresh tokens for a given user.
     *
     * @param userId the user’s database ID
     */
    void revokeAllUserTokens(Long userId);
}