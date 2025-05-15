package com.example.trainbuddy_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.trainbuddy_server.entity.Role;

public class RegisterRequestDto {

    @NotBlank(message = "Le nom d’utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @Email(message = "L’email doit être valide")
    @NotBlank(message = "L’email est obligatoire")
    private String email;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotNull(message = "Le flag isCoach est obligatoire")
    private Boolean isCoach;

    // ========== Getters ==========
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getIsCoach() {
        return isCoach;
    }

    // ========== Setters ==========
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setIsCoach(Boolean isCoach) {
        this.isCoach = isCoach;
    }
}
