package com.example.trainbuddy_server.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.repository.UsersRepository;
import com.example.trainbuddy_server.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repo;

    public UsersServiceImpl(UsersRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Users> findByUsername(String u) {
        return repo.findByUsername(u);
    }

    @Override
    public Optional<Users> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Users create(Users u) {
        return repo.save(u);
    }

    @Override
    public Users update(Users user) {
        // on s’assure que l’ID existe
        if (user.getId() == null || !repo.existsById(user.getId())) {
            throw new IllegalArgumentException("Cannot update non-existent user");
        }
        return repo.save(user);
    }
}
