package com.example.trainbuddy_server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Group;
import com.example.trainbuddy_server.entity.GroupMemberId;
import com.example.trainbuddy_server.entity.GroupMembers;
import com.example.trainbuddy_server.entity.Users;

public interface GroupMembersRepository extends JpaRepository<GroupMembers,GroupMemberId>{

    List<GroupMembers> findByGroup(Group group);
    Optional<GroupMembers> findByGroupAndUser(Group group, Users user);

    List<GroupMembers> findByUser(Users user);
}
