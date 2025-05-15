package com.example.trainbuddy_server.entity;

import java.io.Serializable;
import java.util.Objects;

public class GroupMemberId implements Serializable {
    private Long group;
    private Long user;

    public GroupMemberId() {}
    public GroupMemberId(Long group, Long user) {
        this.group = group;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMemberId)) return false;
        GroupMemberId that = (GroupMemberId) o;
        return Objects.equals(group, that.group) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, user);
    }

    // Getters and Setters
    public Long getGroup() {
        return group;
    }
    public void setGroup(Long group) {
        this.group = group;
    }
    public Long getUser() {
        return user;
    }
    public void setUser(Long user) {
        this.user = user;
    }
}
