package com.example.contractorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ContractorInfo {
    private long id;
    private UUID uuid;
    private String name;
    private String phone;
    private String email;
    private String accountMaganerUsername;
}
