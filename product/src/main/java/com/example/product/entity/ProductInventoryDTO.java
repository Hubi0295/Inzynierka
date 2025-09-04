package com.example.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class ProductInventoryDTO {
    private UUID uuid;
    private String RFID;
    private String name;
    private String category;
    private long spot;
    private String contractor;
    private Timestamp updated_at;
    private String note;
    private Boolean isCorrect;
}
