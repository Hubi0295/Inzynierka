package com.example.warehouse.repository;

import com.example.warehouse.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall,Long> {
    Optional<Hall> findHallByUuid(UUID uuid);
}
