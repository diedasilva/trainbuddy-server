package com.example.trainbuddy_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.GroupMembers;
import com.example.trainbuddy_server.repository.GroupMembersRepository;

@Service
public class GroupMembersService {
    private final GroupMembersRepository repo;
    public GroupMembersService(GroupMembersRepository repo) { this.repo = repo; }

    public List<GroupMembers> getByGroup(Long groupId) {
        return repo.findAll().stream()
            .filter(m -> m.getGroup().getId().equals(groupId))
            .collect(Collectors.toList());
    }

    public GroupMembers add(GroupMembers m) {
        return repo.save(m);
    }
}