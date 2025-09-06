package com.example.product.repository;

import com.example.product.entity.Product;
import com.example.warehouse.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByUuid(UUID uuid);
    Optional<Product> findByRfid(String rfid);
    Optional<Product> findBySpot(Spot spot);
}
