package com.example.productservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductReceiptDTO {
    private List<UUID> products;
    private long contractor;
}
