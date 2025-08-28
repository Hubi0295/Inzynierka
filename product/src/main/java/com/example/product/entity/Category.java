package com.example.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(generator = "categories_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="categories_id_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    private long id;
    @Column(length = 45,nullable = false)
    private String name;
}
