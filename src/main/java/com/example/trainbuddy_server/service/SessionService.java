package com.example.trainbuddy_server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Session;
import com.example.trainbuddy_server.repository.SessionRepository;

@Service
public class SessionService {
    private final SessionRepository repo;
    public SessionService(SessionRepository repo) { this.repo = repo; }

    public List<Session> getAll() {
        return repo.findAll();
    }

    public Session create(Session s) {
        return repo.save(s);
    }
}