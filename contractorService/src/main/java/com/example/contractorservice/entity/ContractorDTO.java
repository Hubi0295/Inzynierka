package com.example.contractorservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ContractorDTO {
    private String name;
    private String phone;
    private String email;
}
