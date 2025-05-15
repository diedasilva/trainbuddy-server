package com.example.trainbuddy_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Session;
import com.example.trainbuddy_server.entity.SessionExercise;
import com.example.trainbuddy_server.entity.SessionMembers;
import com.example.trainbuddy_server.service.SessionExerciseService;
import com.example.trainbuddy_server.service.SessionMembersService;
import com.example.trainbuddy_server.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final SessionMembersService membersService;
    private final SessionExerciseService exerciseService;

    public SessionController(SessionService sessionService,
                             SessionMembersService membersService,
                             SessionExerciseService exerciseService) {
        this.sessionService = sessionService;
        this.membersService = membersService;
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<Session> getAll() {
        return sessionService.getAll();
    }

    @PostMapping
    public Session create(@RequestBody Session s) {
        return sessionService.create(s);
    }

    @GetMapping("/{sessionId}/members")
    public List<SessionMembers> getMembers(@PathVariable Long sessionId) {
        return membersService.getBySession(sessionId);
    }

    @PostMapping("/{sessionId}/members")
    public SessionMembers addMember(@PathVariable Long sessionId, @RequestBody SessionMembers m) {
        return membersService.add(m);
    }

    @GetMapping("/{sessionId}/exercises")
    public List<SessionExercise> getExercises(@PathVariable Long sessionId) {
        return exerciseService.getBySession(sessionId);
    }

    @PostMapping("/{sessionId}/exercises")
    public SessionExercise addExercise(@PathVariable Long sessionId, @RequestBody SessionExercise ex) {
        return exerciseService.add(ex);
    }
}