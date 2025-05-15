package com.example.trainbuddy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbuddy_server.entity.Chart;

public interface ChartRepository extends JpaRepository<Chart, Long> {
}
