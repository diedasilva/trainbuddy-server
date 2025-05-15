package com.example.trainbuddy_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Chart;
import com.example.trainbuddy_server.repository.ChartRepository;

@Service
public class ChartService {
    private final ChartRepository repo;
    public ChartService(ChartRepository repo) { this.repo = repo; }

    public List<Chart> getAll(Long userId, Long groupId) {
        return repo.findAll().stream()
            .filter(c -> userId == null || (c.getUser() != null && c.getUser().getId().equals(userId)))
            .filter(c -> groupId == null || (c.getGroup() != null && c.getGroup().getId().equals(groupId)))
            .collect(Collectors.toList());
    }

    public Chart create(Chart c) {
        return repo.save(c);
    }
}
