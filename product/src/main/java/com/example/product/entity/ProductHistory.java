package com.example.product.entity;

import com.example.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Entity
@Table(name="Producthistory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistory {
    @Id
    @GeneratedValue(generator = "producthistory_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="producthistory_id_seq", sequenceName = "producthistory_id_seq", allocationSize = 1)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="product_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_producthistory_product")
    )
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_producthistory_user")
    )
    private User user;
    @Column(name="action_type")
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private Timestamp created_at;

}
