package com.example.contractorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContractorInfo {
    private long id;
    private String name;
    private String phone;
    private String email;
    private String accountMaganerUsername;
}
