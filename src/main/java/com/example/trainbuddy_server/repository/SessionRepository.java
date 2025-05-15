package com.example.trainbuddy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
