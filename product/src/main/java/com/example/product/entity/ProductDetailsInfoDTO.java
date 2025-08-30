package com.example.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
public class ProductDetailsInfoDTO {
    private String description;
    private float weight;
    private float width;
    private float height;
    private Timestamp created_at;
    private Timestamp updated_at;
}
