package com.example.trainbuddy_server.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.exception.UserNotFoundException;
import com.example.trainbuddy_server.service.TokenService;
import com.example.trainbuddy_server.service.UsersService;

/**
 * Administration endpoints for managing user sessions and tokens.
 * Only users with the ADMIN role may access these operations.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TokenService tokenService;
    private final UsersService usersService;

    public AdminController(TokenService tokenService, UsersService usersService) {
        this.tokenService = tokenService;
        this.usersService = usersService;
    }

    /**
     * Revoke all refresh tokens for a user (logging them out on all clients)
     * and set lastLogoutAt to immediately invalidate any existing access tokens.
     *
     * @param userId the ID of the user to revoke
     * @return HTTP 204 No Content on success
     * @throws UserNotFoundException if the user with given ID does not exist
     */
    @PostMapping("/revoke/{userId}")
    public ResponseEntity<Void> revokeUser(@PathVariable Long userId) {
        Users user = usersService.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        // Revoke all refresh tokens for this user
        tokenService.revokeAllUserTokens(userId);

        user.setLastLogoutAt(Instant.now());

        usersService.update(user);

        return ResponseEntity.noContent().build();
    }
}
