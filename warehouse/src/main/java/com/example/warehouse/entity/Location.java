package com.example.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="locations")
public class Location {
    @Id
    @GeneratedValue(generator = "locations_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "locations_id_seq", sequenceName = "locations_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_warehouse",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_location_warehouse")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Warehouse warehouse;

    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_hall",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_location_hall")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hall hall;


    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_shelf",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_location_shelf")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shelf shelf;


    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_spot",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_location_spot")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Spot spot;

    @Column
    private boolean isfree;

    public Location(long id, UUID uuid, Warehouse warehouse, Hall hall, Shelf shelf, Spot spot, boolean isFree) {
        this.id = id;
        this.uuid = uuid;
        this.warehouse = warehouse;
        this.hall = hall;
        this.shelf = shelf;
        this.spot = spot;
        this.isfree = isFree;
    }



}
