package com.example.trainbuddy_server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.trainbuddy_server.entity.Users;
import com.example.trainbuddy_server.exception.UserConflictException;
import com.example.trainbuddy_server.repository.UsersRepository;

@Service
public class UsersService {

    private final UsersRepository repo;

    public UsersService(UsersRepository repo) {
        this.repo = repo;
    }

    public List<Users> getAll() {
        return repo.findAll();
    }

    public Optional<Users> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Optional<Users> findById(Long id) {
        return repo.findById(id);
    }

    public Users create(Users u) {
        try {
            return repo.save(u);
        } catch (DataIntegrityViolationException ex) {
            String msg = ex.getRootCause().getMessage();
            if (msg.contains("users_username_key")) {
                throw new UserConflictException("Le nom d'utilisateur est déjà utilisé.");
            }
            if (msg.contains("users_email_key")) {
                throw new UserConflictException("L'email est déjà utilisé.");
            }
            throw new UserConflictException("Conflit lors de la création de l'utilisateur.");
        }
    }
}
