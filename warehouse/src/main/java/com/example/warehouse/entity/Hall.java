package com.example.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="halls")
public class Hall {
    @Id
    @GeneratedValue(generator = "halls_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "halls_id_seq", sequenceName = "halls_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @Column(length = 30, nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_warehouse",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_hall_warehouse")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Warehouse warehouse;

    public Hall(long id, UUID uuid, String name, Warehouse warehouse) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.warehouse = warehouse;
    }
}
