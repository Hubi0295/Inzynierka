package com.example.productservice.entity;

import com.example.product.entity.Product;
import com.example.product.entity.ProductInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReceiptInfo {
    private UUID uuid;
    private String contractor;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String document_number;
}
