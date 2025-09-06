package com.example.productservice.repository;

import com.example.productservice.entity.ProductReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductReceiptRepository extends JpaRepository<ProductReceipt,Long> {
    Optional<ProductReceipt> findByUuid(UUID uuid);
}
