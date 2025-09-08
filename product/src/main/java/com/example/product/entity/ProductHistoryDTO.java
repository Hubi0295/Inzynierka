package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProductHistoryDTO {
    private List<ProductHistoryInfo> productHistory;
}
