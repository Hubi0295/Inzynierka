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
    @Query("SELECT p FROM Product p WHERE p.product_receipt = :id")
    List<Product> findByProduct_receipt_id(@Param("id") Long id);
    @Query("SELECT p FROM Product p WHERE p.product_issue = :id")
    List<Product> findByProduct_issue_id(@Param("id") Long id);
}
