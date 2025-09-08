package com.example.product.repository;

import com.example.product.entity.Product;
import com.example.product.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductHistoryRespository extends JpaRepository<ProductHistory,Long> {
    @Query("""
            SELECT p
            FROM ProductHistory p
            WHERE p.product = :prod
            ORDER BY p.created_at ASC
            """)
    List<ProductHistory> findByProductId(@Param("prod") Product prod);
}
