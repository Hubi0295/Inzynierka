package com.example.warehouse.fasada;

import com.example.warehouse.entity.StructureDTO;
import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.service.WarehouseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
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
    public ResponseEntity<?> addWarehouse(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO){
        return warehouseService.addWarehouse(httpRequest, structureDTO);
    }
    @RequestMapping(path="/warehouses", method = RequestMethod.GET)
    public ResponseEntity<?> stateOfWarehouses(HttpServletResponse httpRequest){
        return warehouseService.stateOfWarehouses(httpRequest);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> stateOfWarehouseByUUID(HttpServletResponse httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.stateOfWarehouseByUUID(httpRequest,uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateWarehouse(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO,@PathVariable("uuid") UUID uuid){
        return warehouseService.updateWarehouse(httpRequest, structureDTO,uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWarehouse(HttpServletResponse httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteWarehouse(httpRequest, uuid);
    }
    @RequestMapping(path="/warehouses/{uuid}/halls", method = RequestMethod.POST)
    public ResponseEntity<?> addHall(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.addHall(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/halls/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateHall(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateHall(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/halls/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteHall(HttpServletResponse httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteHall(httpRequest, uuid);
    }
    @RequestMapping(path="/halls/{uuid}/shelves", method = RequestMethod.POST)
    public ResponseEntity<?> addShelf(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.addShelf(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateShelf(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateShelf(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShelf(HttpServletResponse httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteShelf(httpRequest, uuid);
    }
    @RequestMapping(path="/shelves/{uuid}/spots", method = RequestMethod.POST)
    public ResponseEntity<?> addSpot(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.addSpot(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/spots/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSpot(HttpServletResponse httpRequest, @RequestBody StructureDTO structureDTO, @PathVariable("uuid") UUID uuid){
        return warehouseService.updateSpot(httpRequest, structureDTO, uuid);
    }
    @RequestMapping(path="/spots/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSpot(HttpServletResponse httpRequest, @PathVariable("uuid") UUID uuid){
        return warehouseService.deleteSpot(httpRequest, uuid);
    }

}
