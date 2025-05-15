package com.example.trainbuddy_server.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.trainbuddy_server.repository.RevokedTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RevokedTokenRepository revokedRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // Récupération du token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // Si pas encore authentifié dans le contexte et qu'un username existe
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Vérification de la signature et de l'expiration
            if (jwtUtil.validateToken(jwt)) {
                // Extraction du JTI et vérification qu'il n'est pas dans revoked_tokens
                String jti = jwtUtil.extractJti(jwt);
                boolean isRevoked = revokedRepo.existsById(jti);

                if (!isRevoked) {
                    // Création d'un AuthenticationToken sans authorities (à adapter si tu gères des rôles)
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token révoqué");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
