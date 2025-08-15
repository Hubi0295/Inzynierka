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
@Table(name="spots")
public class Spot {
    @Id
    @GeneratedValue(generator = "spots_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "spots_id_seq", sequenceName = "spots_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @Column(length = 30, nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_shelf",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_spot_shelf")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shelf shelf;

    public Spot(long id, UUID uuid, String name, Shelf shelf) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.shelf = shelf;
    }

}
