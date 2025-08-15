package com.example.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(generator = "warehouses_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "warehouses_id_seq", sequenceName = "warehouses_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @Column(length = 30, nullable = false)
    private String name;

    public Warehouse(long id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }
}
