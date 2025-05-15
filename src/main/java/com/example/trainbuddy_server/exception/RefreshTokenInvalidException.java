// 401 – refresh token invalide ou expiré
package com.example.trainbuddy_server.exception;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException() {
        super("Refresh token is invalid or expired");
    }
}
