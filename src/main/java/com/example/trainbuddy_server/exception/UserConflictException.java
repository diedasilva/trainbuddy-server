// 409 – conflit lors de la création d’un utilisateur
package com.example.trainbuddy_server.exception;

public class UserConflictException extends RuntimeException {
    public UserConflictException(String message) {
        super(message);
    }

}
