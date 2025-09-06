package com.example.productservice.entity;

import com.example.product.entity.Product;
import com.example.product.entity.ProductInfoDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class ProductReceiptInfo {
    private List<ProductInfoDTO> products;
    private String contractor;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String document_number;
    private UUID uuid;
}
