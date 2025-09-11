package com.example.auth.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {
    private String content;
    private ReportType type;
}
