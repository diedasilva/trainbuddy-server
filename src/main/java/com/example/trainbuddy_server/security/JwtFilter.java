package com.example.trainbuddy_server.security;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.repository.RevokedTokenRepository;
import com.example.trainbuddy_server.service.UsersService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RevokedTokenRepository revokedRepo;
    private final UsersService usersService;

    public JwtFilter(JwtUtil jwtUtil,
                     RevokedTokenRepository revokedRepo,
                     UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.revokedRepo = revokedRepo;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                String jti = jwtUtil.extractJti(token);

                // 1) Blacklist check
                if (revokedRepo.existsById(jti)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token revoked");
                    return;
                }

                // 2) Admin-forced logout check
                Instant issuedAt = jwtUtil.extractIssuedAt(token);
                Users user = usersService.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
                Instant lastLogout = user.getLastLogoutAt();
                if (lastLogout != null && issuedAt.isBefore(lastLogout)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalidated by admin");
                    return;
                }

                // 3) Extract roles and create authorities
                List<GrantedAuthority> auths = jwtUtil.extractRoles(token).stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                // 4) Build Authentication and set in context
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, auths);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
