package com.example.trainbuddy_server.dto;

import java.time.Instant;

public class ExerciseDto {

    private Long id;
    private String name;
    private String activityType;
    private String defaultConf;
    private Instant createdAt;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDefaultConf() {
        return defaultConf;
    }

    public void setDefaultConf(String defaultConf) {
        this.defaultConf = defaultConf;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
