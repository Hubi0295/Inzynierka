package com.example.warehouse.repository;

import com.example.warehouse.entity.Location;
import com.example.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<List<Location>> findLocationsByWarehouse(Warehouse warehouse);
}
