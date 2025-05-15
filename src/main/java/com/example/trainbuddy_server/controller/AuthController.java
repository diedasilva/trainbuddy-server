package com.example.trainbuddy_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.dto.AuthResponseDto;
import com.example.trainbuddy_server.dto.LoginRequestDto;
import com.example.trainbuddy_server.dto.RefreshRequestDto;
import com.example.trainbuddy_server.dto.RegisterRequestDto;
import com.example.trainbuddy_server.entity.RefreshToken;
import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.security.JwtUtil;
import com.example.trainbuddy_server.service.TokenService;
import com.example.trainbuddy_server.service.UsersService;

import jakarta.validation.Valid;

/**
 * REST controller for authentication endpoints: login, token rotation, and
 * logout.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
            UsersService usersService,
            TokenService tokenService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.usersService = usersService;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user, issue tokens.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto req) {
        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setIsCoach(req.getIsCoach());

        Users saved = usersService.create(user);
        String accessToken = jwtUtil.generateToken(saved.getUsername(), saved.getId());
        RefreshToken rt = tokenService.createRefreshToken(saved.getId(), req.getDeviceId());

        return ResponseEntity.ok(new AuthResponseDto(accessToken, rt.getToken()));
    }

    /**
     * Authenticate a user and issue access + refresh tokens.
     *
     * @param req LoginRequestDto containing username, password, and optional
     * deviceId
     * @return AuthResponseDto with accessToken and refreshToken
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        Users user = usersService.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getId());
        RefreshToken rt = tokenService.createRefreshToken(user.getId(), req.getDeviceId());

        return ResponseEntity.ok(new AuthResponseDto(accessToken, rt.getToken()));
    }

    /**
     * Rotate an existing refresh token: revoke old, issue new, and return new
     * access token.
     *
     * @param body RefreshRequestDto containing the old refreshToken and
     * optional deviceId
     * @return AuthResponseDto with new accessToken and new refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshRequestDto body) {
        RefreshToken newRt = tokenService.rotateRefreshToken(body.getRefreshToken());
        Users user = newRt.getUser();
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getId());
        return ResponseEntity.ok(new AuthResponseDto(accessToken, newRt.getToken()));
    }

    /**
     * Logout by revoking the provided refresh token.
     *
     * @param body RefreshRequestDto containing refreshToken to revoke
     * @return 204 No Content on success
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequestDto body) {
        tokenService.revokeRefreshToken(body.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
