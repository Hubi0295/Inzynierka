package com.example.warehouse.service;

import com.example.warehouse.entity.*;
import com.example.warehouse.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.entity.Response;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final HallRepository hallRepository;
    private final LocationRepository locationRepository;
    private final ShelfRepository shelfRepository;
    private final SpotRepository spotRepository;
    private final WarehouseRepository warehouseRepository;

    public ResponseEntity<?> addWarehouse(HttpServletResponse httpRequest, StructureDTO structureDTO) {
        String name = structureDTO.getName();
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouseRepository.saveAndFlush(warehouse);
        return ResponseEntity.ok().body(new Response("Utworzono magazyn"+name));
    }

    public ResponseEntity<?> addHall(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if(warehouse!= null){
            Hall hall = new Hall();
            hall.setName(name);
            hall.setWarehouse(warehouse);
            hallRepository.saveAndFlush(hall);
            return ResponseEntity.ok().body(new Response("Utworzono hale "+name));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć hali o nazwie "+name));
        }
    }

    public ResponseEntity<?> addShelf(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        if(hall != null){
            Shelf shelf = new Shelf();
            shelf.setName(name);
            shelf.setHall(hall);
            shelfRepository.saveAndFlush(shelf);
            return ResponseEntity.ok().body(new Response("Utworzono regał"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć regału o nazwie "+name));
        }
    }
    @Transactional
    public ResponseEntity<?> addSpot(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Shelf shelf = shelfRepository.findShelfByUuid(uuid).orElse(null);
        if(shelf != null){
            Spot spot = new Spot();
            spot.setName(name);
            spot.setShelf(shelf);
            spotRepository.saveAndFlush(spot);
            Location location = new Location();
            location.setWarehouse(shelf.getHall().getWarehouse());
            location.setHall(shelf.getHall());
            location.setShelf(shelf);
            location.setSpot(spot);
            location.setIsfree(true);
            locationRepository.saveAndFlush(location);
            return ResponseEntity.ok().body(new Response("Utworzono miejsce magazynowe"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć miejsca magazynowego o nazwie "+name));
        }
    }

    public ResponseEntity<?> deleteSpot(HttpServletResponse httpRequest, UUID uuid) {
        Spot spot = spotRepository.findSpotByUuid(uuid).orElse(null);
        if (spot != null) {
            spotRepository.delete(spot);
            return ResponseEntity.ok().body(new Response("Usunięto miejsce magazynowe"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono miejsca magazynowego o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateSpot(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        Spot spot = spotRepository.findSpotByUuid(uuid).orElse(null);
        if (spot != null) {
            spot.setName(structureDTO.getName());
            spotRepository.saveAndFlush(spot);
            return ResponseEntity.ok().body(new Response("Zaktualizowano miejsce magazynowe"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono miejsca magazynowego o podanym UUID"));
        }
    }

    public ResponseEntity<?> deleteShelf(HttpServletResponse httpRequest, UUID uuid) {
        Shelf shelf = shelfRepository.findShelfByUuid(uuid).orElse(null);
        if (shelf != null) {
            shelfRepository.delete(shelf);
            return ResponseEntity.ok().body(new Response("Usunięto regał"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono regału o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateShelf(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        Shelf shelf = shelfRepository.findShelfByUuid(uuid).orElse(null);
        if (shelf != null) {
            shelf.setName(structureDTO.getName());
            shelfRepository.saveAndFlush(shelf);
            return ResponseEntity.ok().body(new Response("Zaktualizowano regał"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono regału o podanym UUID"));
        }
    }

    public ResponseEntity<?> deleteHall(HttpServletResponse httpRequest, UUID uuid) {
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        if (hall != null) {
            hallRepository.delete(hall);
            return ResponseEntity.ok().body(new Response("Usunięto halę"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono hali o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateHall(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        if (hall != null) {
            hall.setName(structureDTO.getName());
            hallRepository.saveAndFlush(hall);
            return ResponseEntity.ok().body(new Response("Zaktualizowano halę"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono hali o podanym UUID"));
        }
    }

    public ResponseEntity<?> deleteWarehouse(HttpServletResponse httpRequest, UUID uuid) {
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if (warehouse != null) {
            warehouseRepository.delete(warehouse);
            return ResponseEntity.ok().body(new Response("Usunięto magazyn"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateWarehouse(HttpServletResponse httpRequest, StructureDTO structureDTO, UUID uuid) {
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if (warehouse != null) {
            warehouse.setName(structureDTO.getName());
            warehouseRepository.saveAndFlush(warehouse);
            return ResponseEntity.ok().body(new Response("Zaktualizowano magazyn"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
    }

    public ResponseEntity<?> stateOfWarehouses(HttpServletResponse httpRequest) {
        List<Location> locations = locationRepository.findAll();
        List<LocationDTO> result = locations.stream().map(loc -> new LocationDTO(
                loc.getWarehouse().getUuid(),
                loc.getWarehouse().getName(),
                loc.getHall().getUuid(),
                loc.getHall().getName(),
                loc.getShelf().getUuid(),
                loc.getShelf().getName(),
                loc.getSpot().getUuid(),
                loc.getSpot().getName(),
                loc.isIsfree()
        )).toList();
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> stateOfWarehouseByUUID(HttpServletResponse httpRequest, UUID uuid) {
        List<Location> locations = locationRepository.findByUuid(uuid).orElse(null);
        if(locations!=null && locations.size()>0){
            List<LocationDTO> result = locations.stream().map(loc -> new LocationDTO(
                    loc.getWarehouse().getUuid(),
                    loc.getWarehouse().getName(),
                    loc.getHall().getUuid(),
                    loc.getHall().getName(),
                    loc.getShelf().getUuid(),
                    loc.getShelf().getName(),
                    loc.getSpot().getUuid(),
                    loc.getSpot().getName(),
                    loc.isIsfree()
            )).toList();
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
    }
}
