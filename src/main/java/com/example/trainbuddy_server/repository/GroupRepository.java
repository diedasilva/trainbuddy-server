package com.example.trainbuddy_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Group;

public interface GroupRepository extends JpaRepository<Group,Long>{
    boolean existsByName(String name);

    List<Group> findByCreatedBy_Id(Long userId);
}
