package com.example.trainbuddy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.RevokedToken;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
}
