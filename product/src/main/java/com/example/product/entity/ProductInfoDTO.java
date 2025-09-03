package com.example.product.entity;

import com.example.contractorservice.entity.Contractor;
import com.example.warehouse.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductInfoDTO {
    private UUID uuid;
    private String RFID;
    private String name;
    private Category category;
    private Spot spot;
    private String contractor;
}
