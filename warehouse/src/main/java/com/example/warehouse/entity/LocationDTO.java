package com.example.warehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LocationDTO {
    private UUID warehouseUuid;
    private String warehouseName;
    private UUID hallUuid;
    private String hallName;
    private UUID shelfUuid;
    private String shelfName;
    private UUID spotUuid;
    private String spotName;
    private boolean isFree;
}
