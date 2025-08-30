package com.example.product.entity;

import com.example.warehouse.entity.Location;
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
    private Location location;
    private long contractor;
}
