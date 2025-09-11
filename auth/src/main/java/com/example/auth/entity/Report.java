package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(generator = "reports_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "reports_id_seq", sequenceName = "reports_id_seq", allocationSize = 1)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fk_report_user")
    )
    private User user;
    @Enumerated(EnumType.STRING)
    private ReportType type;
    private Timestamp created_at;
    private String content;
}
