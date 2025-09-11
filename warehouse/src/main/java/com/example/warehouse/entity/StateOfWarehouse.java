package com.example.warehouse.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StateOfWarehouse {
    private List<SpotDTO> locations;
    private Map<Long,Long> numberFree;
    private Map<Long,Long>  numberAll;

}
