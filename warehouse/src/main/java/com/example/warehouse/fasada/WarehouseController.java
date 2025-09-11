package com.example.warehouse.fasada;

import com.example.warehouse.entity.StructureDTO;
import com.example.warehouse.service.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/warehouseManagement")
public class WarehouseController {
    private final WarehouseService warehouseService;
    public WarehouseController(WarehouseService warehouseService){
        this.warehouseService=warehouseService;
    }
    @RequestMapping(path="/warehouses", method = RequestMethod.POST)
    public ResponseEntity<?> addWarehouse(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO){
        return warehouseService.addWarehouse(httpRequest, structureDTO);
    }
    @RequestMapping(path="/warehouses", method = RequestMethod.GET)
    public ResponseEntity<?> stateOfWarehouses(HttpServletRequest httpRequest){
        return warehouseService.stateOfWarehouses(httpRequest);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> stateOfWarehouseByUUID(HttpServletRequest httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.stateOfWarehouseByUUID(httpRequest,uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateWarehouse(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO,@PathVariable("uuid") UUID uuid){
        return warehouseService.updateWarehouse(httpRequest, structureDTO,uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWarehouse(HttpServletRequest httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteWarehouse(httpRequest, uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}/halls", method = RequestMethod.POST)
    public ResponseEntity<?> addHall(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.addHall(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/halls/{uuid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateHall(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateHall(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/halls/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteHall(HttpServletRequest httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteHall(httpRequest, uuid);
    }
    @RequestMapping(path="/halls/{uuid}/shelves", method = RequestMethod.POST)
    public ResponseEntity<?> addShelf(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){

        return warehouseService.addShelf(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateShelf(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateShelf(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShelf(HttpServletRequest httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteShelf(httpRequest, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}/spots", method = RequestMethod.POST)
    public ResponseEntity<?> addSpot(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.addSpot(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/spots/{uuid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateSpot(HttpServletRequest httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateSpot(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/spots/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSpot(HttpServletRequest httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteSpot(httpRequest, uuid);
    }
    @RequestMapping(value = "/warehouses/report", method = RequestMethod.GET)
    public ResponseEntity<?> getReportStateOfWarehouses(HttpServletRequest httpServletRequest){
        return warehouseService.getReportStateOfWarehouses(httpServletRequest);
    }
    @RequestMapping(value = "/warehouses/freeSpots", method = RequestMethod.GET)
    public ResponseEntity<?> getFreeSpots(){
        return warehouseService.getFreeSpots();
    }

}
