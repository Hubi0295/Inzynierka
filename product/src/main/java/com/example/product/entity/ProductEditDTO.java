package com.example.product.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductEditDTO {
    private String rfid;
    private String name;
    private int category;
    private String description;
    private float weight;
    private float height;
    private float width;
    private int contractor;
}
