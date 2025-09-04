package com.example.warehouse.repository;

import com.example.warehouse.entity.Hall;
import com.example.warehouse.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShelfRepository extends JpaRepository<Shelf,Long> {
    Optional<Shelf> findShelfByUuid(UUID uuid);
    List<Shelf> findAllByHall(Hall hall);
}
