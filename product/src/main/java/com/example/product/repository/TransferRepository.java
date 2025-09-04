package com.example.product.repository;

import com.example.product.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
}
