package com.example.trainbuddy_server.entity;

import java.io.Serializable;
import java.util.Objects;

public class SessionMemberId implements Serializable {

    private Long session;
    private Long user;

    public SessionMemberId() {
    }

    public SessionMemberId(Long session, Long user) {
        this.session = session;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionMemberId)) {
            return false;
        }
        SessionMemberId that = (SessionMemberId) o;
        return Objects.equals(session, that.session) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, user);
    }
    
    // Getters and Setters
    public Long getSession() {
        return session;
    }

    public void setSession(Long session) {
        this.session = session;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
