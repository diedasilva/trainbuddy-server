package com.example.trainbuddy_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByActivityType(String activityType);
}