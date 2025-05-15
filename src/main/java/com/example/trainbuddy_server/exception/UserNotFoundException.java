package com.example.trainbuddy_server.exception;

/**
 * Thrown when a user is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
