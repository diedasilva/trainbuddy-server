package com.example.trainbuddy_server.service.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trainbuddy_server.entity.RefreshToken;
import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.exception.RefreshTokenInvalidException;
import com.example.trainbuddy_server.exception.UserNotFoundException;
import com.example.trainbuddy_server.repository.RefreshTokenRepository;
import com.example.trainbuddy_server.security.JwtUtil;
import com.example.trainbuddy_server.service.TokenService;
import com.example.trainbuddy_server.service.UsersService;

/**
 * Service implementation for managing refresh tokens lifecycle,
 * including creation, rotation (one-time use), and revocation.
 */
@Service
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshRepo;
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final SecureRandom random = new SecureRandom();

    public TokenServiceImpl(RefreshTokenRepository refreshRepo,
                            UsersService usersService,
                            JwtUtil jwtUtil) {
        this.refreshRepo  = refreshRepo;
        this.usersService = usersService;
        this.jwtUtil      = jwtUtil;
    }

    /**
     * Creates a new refresh token for the specified user.
     * The token is a secure random string and will expire after the configured duration.
     * @param userId   the ID of the user for whom the token is created
     * @param deviceId optional device identifier for session tracking
     * @return the persisted RefreshToken entity
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public RefreshToken createRefreshToken(Long userId, String deviceId) {
        Users user = usersService.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        String token = generateRandomToken();
        RefreshToken rt = new RefreshToken();
        rt.setToken(token);
        rt.setUser(user);
        rt.setDeviceId(deviceId);
        rt.setExpiresAt(Instant.now().plusMillis(jwtUtil.getRefreshExpiration()));
        return refreshRepo.save(rt);
    }

    /**
     * Rotates an existing refresh token by invalidating the old one and issuing a new token.
     * Ensures that the old token is still valid (not expired or already used).
     * @param oldToken the value of the existing refresh token to rotate
     * @return the newly created RefreshToken entity
     * @throws RefreshTokenInvalidException if the old token is invalid, expired, or revoked
     */
    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(String oldToken) {
        RefreshToken existing = refreshRepo.findByTokenAndRevokedAtIsNull(oldToken)
            .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()))
            .orElseThrow(RefreshTokenInvalidException::new);

        existing.setRevokedAt(Instant.now());
        
        return createRefreshToken(existing.getUser().getId(), existing.getDeviceId());
    }

    /**
     * Revokes a single refresh token, marking it as no longer valid.
     * @param token the value of the refresh token to revoke
     */
    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        refreshRepo.findByTokenAndRevokedAtIsNull(token)
            .ifPresent(rt -> rt.setRevokedAt(Instant.now()));
    }

    /**
     * Revokes all refresh tokens for a given user, typically used for logout-all or admin actions.
     * @param userId the ID of the user whose tokens will be revoked
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshRepo.deleteAllByUserId(userId);
    }

    /**
     * Generates a secure random token string, URL-safe Base64 encoded.
     * @return a new random token
     */
    private String generateRandomToken() {
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
