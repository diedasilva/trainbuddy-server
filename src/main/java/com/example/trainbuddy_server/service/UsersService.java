package com.example.trainbuddy_server.service;

import java.util.Optional;

import com.example.trainbuddy_server.entity.Users;

/**
 * Service for user CRUD operations.
 */
public interface UsersService {

    Optional<Users> findByUsername(String username);

    /**
     * Persist a new user (registration).
     *
     * @param user the new user to create
     * @return the saved user entity
     */
    Users create(Users user);

    /**
     * Update an existing user.
     *
     * @param user the user entity with updated fields
     * @return the updated user entity
     */
    Users update(Users user);

    Optional<Users> findById(Long id);
}
