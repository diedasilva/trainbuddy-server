package com.example.trainbuddy_server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Exercise;
import com.example.trainbuddy_server.repository.ExerciseRepository;

@Service
public class ExerciseService {
    private final ExerciseRepository repo;
    public ExerciseService(ExerciseRepository repo) { this.repo = repo; }

    public List<Exercise> getAll(String activityType) {
        return activityType == null ? repo.findAll() : repo.findByActivityType(activityType);
    }

    public Exercise create(Exercise e) {
        return repo.save(e);
    }
}