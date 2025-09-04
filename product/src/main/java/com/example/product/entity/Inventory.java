package com.example.product.entity;

import com.example.auth.entity.User;
import com.example.warehouse.entity.Hall;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="inventories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(generator = "inventories_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="inventories_id_seq", sequenceName = "inventories_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    private Timestamp date;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="supervisor_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_inventory_user")
    )
    private User supervisor;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="hall_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_inventory_hall")
    )
    private Hall hall;
}
