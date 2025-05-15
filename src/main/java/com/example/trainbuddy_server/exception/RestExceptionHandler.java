package com.example.trainbuddy_server.exception;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.trainbuddy_server.dto.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;

/**
 * Gestion centralisée des exceptions REST.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /** 422 – Erreurs de validation DTO (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleDtoValidation(MethodArgumentNotValidException ex) {
        List<Map<String,String>> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(f -> Map.of("field", f.getField(), "message", f.getDefaultMessage()))
            .collect(Collectors.toList());
        logger.warn("DTO validation failed: {}", details);
        ErrorResponse body = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Validation Error",
            "One or more fields are invalid",
            details
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /** 422 – Violation de contraintes simples (@Validated) */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
        List<Map<String,String>> details = ex.getConstraintViolations()
            .stream()
            .map(v -> Map.of("field", v.getPropertyPath().toString(), "message", v.getMessage()))
            .collect(Collectors.toList());
        logger.warn("Constraint violations: {}", details);
        ErrorResponse body = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Validation Error",
            "Constraint violations occurred",
            details
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /** 409 – Conflits en base (unicité, UserConflictException) */
    @ExceptionHandler({DataIntegrityViolationException.class, UserConflictException.class})
    public ResponseEntity<ErrorResponse> handleConflict(Exception ex) {
        String message = ex instanceof UserConflictException
            ? ex.getMessage()
            : "Database conflict";
        logger.warn("Conflict: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            message
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /** 401 – Mauvaises identifiants */
    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthFailure(Exception ex) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /** 401 – JWT expiré ou invalide */
    @ExceptionHandler({ExpiredJwtException.class, JwtException.class, RefreshTokenInvalidException.class})
    public ResponseEntity<ErrorResponse> handleJwtErrors(Exception ex) {
        String msg = ex instanceof ExpiredJwtException
            ? "Token has expired"
            : ex.getMessage();
        logger.warn("JWT error: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            msg
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /** 404 – Ressource introuvable */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /** 500 – Erreur serveur inattendue */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        logger.error("Unexpected error", ex);
        ErrorResponse body = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
