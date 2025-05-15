package com.example.trainbuddy_server.exception;

import com.example.trainbuddy_server.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

  // 400 – Validation @Valid sur DTO
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult()
                   .getFieldErrors()
                   .stream()
                   .map(f -> f.getField() + " : " + f.getDefaultMessage())
                   .collect(Collectors.joining(", "));
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", msg);
  }

  // 400 – Violation de contraintes JPA (unicité, non-null…)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
    String root = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
    String msg = "Conflit en base";
    if (root.contains("users_username_key")) {
      msg = "Le nom d'utilisateur est déjà utilisé.";
    } else if (root.contains("users_email_key")) {
      msg = "L'email est déjà utilisé.";
    }
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", msg);
  }

  // 400 – Violation de @Validated sur des champs simples
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
    String msg = ex.getConstraintViolations()
                   .stream()
                   .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                   .collect(Collectors.joining(", "));
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", msg);
  }

  // 400 – Conflit métier explicite
  @ExceptionHandler(UserConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(UserConflictException ex) {
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
  }

  // 401 – Mauvais identifiants
  @ExceptionHandler({ InvalidCredentialsException.class, BadCredentialsException.class })
  public ResponseEntity<ErrorResponse> handleAuth(Exception ex) {
    return buildError(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
  }

  // 404 – Ressource introuvable
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
    return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
  }

  // 500 – toute autre erreur
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
    return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Erreur serveur inattendue");
  }

  private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String error, String message) {
    ErrorResponse body = new ErrorResponse(status.value(), error, message);
    return ResponseEntity.status(status).body(body);
  }
}
