package com.example.trainbuddy_server.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {

    @Id
    @Column(length = 100, nullable = false)
    private String jti;

    @Column(name = "revoked_at", nullable = false, updatable = false)
    private Instant revokedAt;

    @PrePersist
    protected void onCreate() {
        revokedAt = Instant.now();
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }
}
