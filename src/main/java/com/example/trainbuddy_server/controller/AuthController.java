package com.example.trainbuddy_server.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.dto.AuthResponseDto;
import com.example.trainbuddy_server.dto.LoginRequestDto;
import com.example.trainbuddy_server.dto.RegisterRequestDto;
import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.service.UsersService;
import com.example.trainbuddy_server.exception.InvalidCredentialsException;
import com.example.trainbuddy_server.security.JwtUtil;
import com.example.trainbuddy_server.util.CookieUtils;
import com.example.trainbuddy_server.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsersService userService;
    private final PasswordEncoder pwdEncoder;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil,
            UsersService userService, PasswordEncoder pwdEncoder, TokenService tokenService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.pwdEncoder = pwdEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto req, 
                                                HttpServletResponse response) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException();
        }
        Users u = userService.findByUsername(req.getUsername()).orElseThrow();
        String accessToken = jwtUtil.generateToken(u.getUsername(), u.getId());
        String refreshToken = tokenService.createRefreshToken(u.getId()).getToken();
        ResponseCookie cookie = CookieUtils.createHttpOnlyCookie("refreshToken", refreshToken, jwtUtil.getRefreshExpiration());
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(new AuthResponseDto(accessToken, jwtUtil.getExpiration()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto req, 
                                                   HttpServletResponse response) {

        Users u = new Users();

        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(pwdEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        u.setIsCoach(req.getIsCoach());

        Users saved = userService.create(u);

        String accessToken = jwtUtil.generateToken(saved.getUsername(), saved.getId());
        String refreshToken = tokenService.createRefreshToken(saved.getId()).getToken();
        ResponseCookie cookie = CookieUtils.createHttpOnlyCookie("refreshToken", refreshToken, jwtUtil.getRefreshExpiration());
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new AuthResponseDto(accessToken, jwtUtil.getExpiration()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(HttpServletRequest request,
                                                   HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
        return tokenService.validateRefreshToken(refreshToken)
            .map(rt -> {
                Users user = rt.getUser();
                String newAccess = jwtUtil.generateToken(user.getUsername(), user.getId());
                return ResponseEntity.ok(new AuthResponseDto(newAccess, jwtUtil.getExpiration()));
            })
            .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
                                       HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
        tokenService.revokeRefreshToken(refreshToken);
        ResponseCookie cleared = CookieUtils.clearCookie("refreshToken");
        response.addHeader("Set-Cookie", cleared.toString());
        return ResponseEntity.noContent().build();
    }
}
