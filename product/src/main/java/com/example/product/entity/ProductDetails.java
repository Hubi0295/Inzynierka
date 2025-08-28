package com.example.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="productdetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {
    @Id
    @GeneratedValue(generator = "productdetails_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="productdetails_id_seq", sequenceName = "productdetails_id_seq", allocationSize = 1)
    private long id;
    private String description;
    private float weight;
    private float width;
    private float height;
    private Timestamp created_at;
    private Timestamp updated_at;
}
