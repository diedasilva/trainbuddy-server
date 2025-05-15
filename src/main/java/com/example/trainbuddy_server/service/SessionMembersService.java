package com.example.trainbuddy_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.SessionMembers;
import com.example.trainbuddy_server.repository.SessionMembersRepository;

@Service
public class SessionMembersService {
    private final SessionMembersRepository repo;
    public SessionMembersService(SessionMembersRepository repo) { this.repo = repo; }

    public List<SessionMembers> getBySession(Long sessionId) {
        return repo.findAll().stream()
            .filter(m -> m.getSession().getId().equals(sessionId))
            .collect(Collectors.toList());
    }

    public SessionMembers add(SessionMembers m) {
        return repo.save(m);
    }
}