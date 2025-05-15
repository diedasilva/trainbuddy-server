package com.example.trainbuddy_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Chart;
import com.example.trainbuddy_server.service.ChartService;

@RestController
@RequestMapping("/api/charts")
public class ChartController {
    private final ChartService service;
    public ChartController(ChartService service) { this.service = service; }

    @GetMapping
    public List<Chart> getAll(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long groupId
    ) {
        return service.getAll(userId, groupId);
    }

    @PostMapping
    public Chart create(@RequestBody Chart c) {
        return service.create(c);
    }
}