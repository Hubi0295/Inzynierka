package com.example.warehouse.repository;

import com.example.warehouse.entity.ReportDTO;
import com.example.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
    Optional<Warehouse> findWarehouseByUuid(UUID uuid);

}
