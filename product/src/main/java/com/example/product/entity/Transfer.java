package com.example.product.entity;

import com.example.auth.entity.User;
import com.example.warehouse.entity.Hall;
import com.example.warehouse.entity.Spot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="transfers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(generator = "transfers_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="transfers_id_seq", sequenceName = "transfers_id_seq", allocationSize = 1)
    private long id;
    private Timestamp date;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="product_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_transfer_product")
    )
    private Product product;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="supervisor_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_transfer_user")
    )
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="spot_from_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_transfer_spot_from")
    )
    private Spot spot_from;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="spot_to_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_transfer_spot_to")
    )
    private Spot spot_to;

}
