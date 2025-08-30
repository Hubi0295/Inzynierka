package com.example.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.sql.Timestamp;
import java.util.UUID;
import com.example.warehouse.entity.Location;
import com.example.auth.entity.User;
@Entity
@Table(name="products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "products_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="products_id_seq", sequenceName = "products_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    private String rfid;
    private String name;
    private long product_receipt_id;
    private long product_issue_id;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="category_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_product_category")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;


    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(
            name="product_details_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_product_productdetails")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductDetails productDetails;


    @ManyToOne(optional = false)
    @JoinColumn(
            name="location_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_product_location")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Location location;

    @Column(name = "contractor_id")
    private long contractor;

    @ManyToOne(optional = false)
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_product_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    private boolean is_active;
    private Timestamp created_at;
    private Timestamp updated_at;
}
