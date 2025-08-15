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
@Table(name="shelves")
public class Shelf {
    @Id
    @GeneratedValue(generator = "shelves_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "shelves_id_seq", sequenceName = "shelves_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @Column(length = 30, nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="id_hall",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_shelf_hall")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hall hall;

    public Shelf(long id, UUID uuid, String name, Hall hall) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.hall = hall;
    }

}
