package com.example.trainbuddy_server.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercises")
public class Exercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "default_conf", columnDefinition = "jsonb")
    private String defaultConf;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "exercise")
    private Set<SessionExercise> sessionLinks;

    // getters & setters
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

    public Set<SessionExercise> getSessionLinks() {
        return sessionLinks;
    }
    public void setSessionLinks(Set<SessionExercise> sessionLinks) {
        this.sessionLinks = sessionLinks;
    }
    
}
