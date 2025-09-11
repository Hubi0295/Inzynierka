package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ReportInfo {
    private long id;
    private long user_id;
    private String userName;
    private String type;
    private Timestamp created_at;
    private String content;
}
