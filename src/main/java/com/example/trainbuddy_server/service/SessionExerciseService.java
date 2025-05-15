package com.example.trainbuddy_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.SessionExercise;
import com.example.trainbuddy_server.repository.SessionExerciseRepository;

@Service
public class SessionExerciseService {
    private final SessionExerciseRepository repo;
    public SessionExerciseService(SessionExerciseRepository repo) { this.repo = repo; }

    public List<SessionExercise> getBySession(Long sessionId) {
        return repo.findAll().stream()
            .filter(e -> e.getSession().getId().equals(sessionId))
            .collect(Collectors.toList());
    }

    public SessionExercise add(SessionExercise e) {
        return repo.save(e);
    }
}