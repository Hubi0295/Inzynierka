package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductDTO {
    private int RFID;
    private String name;
    private Category category;
    private String description;
    private float weight;
    private float height;
    private float width;
    private int location;
    private int contractor;
}
