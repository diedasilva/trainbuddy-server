package com.example.trainbuddy_server.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}