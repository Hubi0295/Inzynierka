package com.example.contractorservice.repository;

import com.example.contractorservice.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContractorRepository extends JpaRepository<Contractor,Long> {
    Optional<Contractor> findContractorByUuid(UUID uuid);
}
