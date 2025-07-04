package com.example.trainbuddy_server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.service.UsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @PostMapping
    public Users create(@RequestBody Users u) {
        return service.create(u);
    }
}
