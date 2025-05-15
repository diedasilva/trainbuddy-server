package com.example.trainbuddy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.SessionExercise;
import com.example.trainbuddy_server.entity.SessionExerciseId;

public interface SessionExerciseRepository extends JpaRepository<SessionExercise, SessionExerciseId> {
}
