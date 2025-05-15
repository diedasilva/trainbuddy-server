package com.example.trainbuddy_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Exercise;
import com.example.trainbuddy_server.service.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService service;
    public ExerciseController(ExerciseService service) { this.service = service; }

    @GetMapping
    public List<Exercise> getAll(@RequestParam(required = false) String activityType) {
        return service.getAll(activityType);
    }

    @PostMapping
    public Exercise create(@RequestBody Exercise e) {
        return service.create(e);
    }
}