package com.example.trainbuddy_server.service.impl;

import com.example.trainbuddy_server.entity.RefreshToken;
import com.example.trainbuddy_server.entity.RevokedToken;
import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.service.UsersService;
import com.example.trainbuddy_server.repository.RefreshTokenRepository;
import com.example.trainbuddy_server.repository.RevokedTokenRepository;
import com.example.trainbuddy_server.security.JwtUtil;
import com.example.trainbuddy_server.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshRepo;
    private final RevokedTokenRepository revokedRepo;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;
    private final SecureRandom random = new SecureRandom();

    public TokenServiceImpl(RefreshTokenRepository refreshRepo,
            RevokedTokenRepository revokedRepo, JwtUtil jwtUtil,
            UsersService usersService) {
        this.refreshRepo = refreshRepo;
        this.revokedRepo = revokedRepo;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        Users user = usersService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + userId));

        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken rt = new RefreshToken();
        rt.setToken(token);
        rt.setUser(user);
        
        rt.setExpiresAt(Instant.now().plusMillis(jwtUtil.getRefreshExpiration()));
        return refreshRepo.save(rt);
    }

    @Override
    public Optional<RefreshToken> validateRefreshToken(String token) {
        return refreshRepo.findByTokenAndRevokedFalse(token)
                .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()));
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        refreshRepo.findById(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshRepo.save(rt);
        });
    }

    @Override
    @Transactional
    public void revokeUserTokens(Long userId) {
        refreshRepo.deleteByUserId(userId);
    }

    @Override
    public void revokeAccessTokenJti(String jti) {
        RevokedToken rj = new RevokedToken();
        rj.setJti(jti);
        revokedRepo.save(rj);
    }
}
