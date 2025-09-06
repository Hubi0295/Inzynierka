package com.example.productservice.entity;

import com.example.auth.entity.User;
import com.example.contractorservice.entity.Contractor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="productreceipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReceipt {
    @Id
    @GeneratedValue(generator = "productreceipts_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="productreceipts_id_seq", sequenceName = "productreceipts_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    @ManyToOne
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_productservice_user")
    )
    private User user;
    @ManyToOne
    @JoinColumn(
            name="contractor_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_productservice_contractor")
    )
    private Contractor contractor;
    private String document_number;
    private Timestamp created_at;
    private Timestamp updated_at;
}
