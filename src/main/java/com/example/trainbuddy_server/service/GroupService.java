package com.example.trainbuddy_server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Group;
import com.example.trainbuddy_server.repository.GroupRepository;

@Service
public class GroupService {
    private final GroupRepository repo;
    public GroupService(GroupRepository repo) { this.repo = repo; }

    public List<Group> getAll() {
        return repo.findAll();
    }

    public Group create(Group g) {
        return repo.save(g);
    }
}