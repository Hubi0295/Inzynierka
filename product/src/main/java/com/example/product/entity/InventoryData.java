package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class InventoryData {
    private HashMap<String, List<ProductInventoryDTO>> inventory = new HashMap<>();
}
