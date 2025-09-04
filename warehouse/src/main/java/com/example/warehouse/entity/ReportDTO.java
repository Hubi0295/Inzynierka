package com.example.warehouse.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ReportDTO {
    public ReportDTO(
            long warehouseId, UUID warehouseUuid, String warehouseName,
            long hallId, UUID hallUuid, String hallName,
            long shelfId, UUID shelfUuid, String shelfName,
            long spotId, UUID spotUuid, String spotName,
            Boolean is_free
    ) {
        this.warehouse_id = warehouseId;
        this.warehouse_uuid = warehouseUuid;
        this.warehouse_name = warehouseName;
        this.hall_id = hallId;
        this.hall_uuid = hallUuid;
        this.hall_name = hallName;
        this.shelf_id = shelfId;
        this.shelf_uuid = shelfUuid;
        this.shelf_name = shelfName;
        this.spot_id = spotId;
        this.spot_uuid = spotUuid;
        this.spot_name = spotName;
        this.is_free = is_free;
    }

    private long warehouse_id;
    private UUID warehouse_uuid;
    private String warehouse_name;
    private long hall_id;
    private UUID hall_uuid;
    private String hall_name;
    private long shelf_id;
    private UUID shelf_uuid;
    private String shelf_name;
    private long spot_id;
    private UUID spot_uuid;
    private String spot_name;
    private Boolean is_free;
}

