package com.example.product.entity;

import com.example.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ProductHistoryInfo {
    private long id;
    private ProductInfoDTO product;
    private String user;
    private ActionType actionType;
    private Timestamp created_at;
}
