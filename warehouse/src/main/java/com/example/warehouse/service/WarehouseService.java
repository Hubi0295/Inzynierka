package com.example.warehouse.service;


import com.example.warehouse.entity.*;
import com.example.warehouse.repository.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
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
//    public ResponseEntity<?> validateToken(HttpServletRequest httpRequest){
//        try{
//            userService.validate_JWT_Token(httpRequest);
//            ResponseEntity<?> r = userService.authorize(httpRequest, UserType.SUPERVISOR);
//            if(r.getStatusCode()!=HttpStatus.OK){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Token nie posiada uprawnień"));
//            }
//            else{
//                return ResponseEntity.ok(new Response("Poprawny token"));
//            }
//        }
//        catch (ExpiredJwtException | IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Token nie wazny"));
//        }
//    }

    public ResponseEntity<?> addWarehouse(HttpServletRequest httpRequest, StructureDTO structureDTO) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        String name = structureDTO.getName();
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouseRepository.saveAndFlush(warehouse);
        return ResponseEntity.ok().body(new Response("Utworzono magazyn"+name));
    }

    public ResponseEntity<?> addHall(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> addShelf(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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
    public ResponseEntity<?> addSpot(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> deleteSpot(HttpServletRequest httpRequest, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        Spot spot = spotRepository.findSpotByUuid(uuid).orElse(null);
        if (spot != null) {
            spotRepository.delete(spot);
            return ResponseEntity.ok().body(new Response("Usunięto miejsce magazynowe"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono miejsca magazynowego o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateSpot(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> deleteShelf(HttpServletRequest httpRequest, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        Shelf shelf = shelfRepository.findShelfByUuid(uuid).orElse(null);
        if (shelf != null) {
            shelfRepository.delete(shelf);
            return ResponseEntity.ok().body(new Response("Usunięto regał"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono regału o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateShelf(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> deleteHall(HttpServletRequest httpRequest, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        if (hall != null) {
            hallRepository.delete(hall);
            return ResponseEntity.ok().body(new Response("Usunięto halę"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono hali o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateHall(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> deleteWarehouse(HttpServletRequest httpRequest, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if (warehouse != null) {
            warehouseRepository.delete(warehouse);
            return ResponseEntity.ok().body(new Response("Usunięto magazyn"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
    }

    public ResponseEntity<?> updateWarehouse(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> stateOfWarehouses(HttpServletRequest httpRequest) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
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

    public ResponseEntity<?> stateOfWarehouseByUUID(HttpServletRequest httpRequest, UUID uuid) {
//        ResponseEntity<?> response = validateToken(httpRequest);
//        if(response.getStatusCode()!=HttpStatus.OK){
//            return response;
//        }
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if(warehouse==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
        List<Location> locations = locationRepository.findLocationsByWarehouse(warehouse).orElse(null);
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
