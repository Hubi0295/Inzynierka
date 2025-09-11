package com.example.warehouse.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class SpotDTO {
    private long id;
    private UUID spot_uuid;
    private String spot_name;
    private long shelf;
    private UUID shelf_uuid;
    private String shelf_name;
    private long hall;
    private UUID hall_uuid;
    private String hall_name;
    private long warehouse;
    private UUID warehouse_uuid;
    private String warehouse_name;
    private boolean is_free;
}