package com.example.warehouse.repository;

import com.example.warehouse.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot,Long> {
    Optional<Spot> findSpotByUuid(UUID uuid);
}
