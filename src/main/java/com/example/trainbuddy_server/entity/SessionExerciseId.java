package com.example.trainbuddy_server.entity;

import java.io.Serializable;
import java.util.Objects;

public class SessionExerciseId implements Serializable {

    private Long session;
    private Long exercise;

    public SessionExerciseId() {
    }

    public SessionExerciseId(Long session, Long exercise) {
        this.session = session;
        this.exercise = exercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionExerciseId)) {
            return false;
        }
        SessionExerciseId that = (SessionExerciseId) o;
        return Objects.equals(session, that.session) && Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, exercise);
    }

    // Getters and Setters
    public Long getSession() {
        return session;
    }

    public void setSession(Long session) {
        this.session = session;
    }

    public Long getExercise() {
        return exercise;
    }

    public void setExercise(Long exercise) {
        this.exercise = exercise;
    }
}
