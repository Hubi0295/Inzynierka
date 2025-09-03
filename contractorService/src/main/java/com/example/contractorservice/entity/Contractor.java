package com.example.contractorservice.entity;

import com.example.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name="contractors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contractor {
    @Id
    @GeneratedValue(generator = "contractors_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="contractors_id_seq", sequenceName = "contractors_id_seq", allocationSize = 1)
    private long id;
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String phone;
    private String email;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="account_manager_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_contractor_supervisor")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User account_manager;
}
