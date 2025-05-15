package com.example.trainbuddy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.SessionMemberId;
import com.example.trainbuddy_server.entity.SessionMembers;

public interface SessionMembersRepository extends JpaRepository<SessionMembers, SessionMemberId> {
}
