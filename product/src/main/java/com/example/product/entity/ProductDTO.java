package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductDTO {
    private String rfid;
    private String name;
    private int category;
    private String description;
    private float weight;
    private float height;
    private float width;
    private int location;
    private int contractor;
}
