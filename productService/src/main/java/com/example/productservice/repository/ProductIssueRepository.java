package com.example.productservice.repository;

import com.example.productservice.entity.ProductIssue;
import com.example.productservice.entity.ProductReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductIssueRepository extends JpaRepository<ProductIssue,Long> {
    Optional<ProductIssue> findByUuid(UUID uuid);

}
