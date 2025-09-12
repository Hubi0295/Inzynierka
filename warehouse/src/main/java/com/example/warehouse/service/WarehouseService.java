package com.example.warehouse.service;
import com.example.warehouse.entity.*;
import com.example.warehouse.repository.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final HallRepository hallRepository;
    private final ShelfRepository shelfRepository;
    private final SpotRepository spotRepository;
    private final WarehouseRepository warehouseRepository;
    public ResponseEntity<?> addWarehouse(HttpServletRequest httpRequest, StructureDTO structureDTO) {
        String name = structureDTO.getName();
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouseRepository.saveAndFlush(warehouse);
        return ResponseEntity.ok().body(new WarehouseResponse("Utworzono magazyn "+name,warehouse.getUuid()));
    }

    public ResponseEntity<?> addHall(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if(warehouse!= null){
            Hall hall = new Hall();
            hall.setName(name);
            hall.setWarehouse(warehouse);
            hallRepository.saveAndFlush(hall);
            return ResponseEntity.ok().body(new WarehouseResponse("Utworzono hale "+name, hall.getUuid()));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć hali o nazwie "+name));
        }
    }

    public ResponseEntity<?> addShelf(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        if(hall != null){
            Shelf shelf = new Shelf();
            shelf.setName(name);
            shelf.setHall(hall);
            shelfRepository.saveAndFlush(shelf);
            return ResponseEntity.ok().body(new WarehouseResponse("Utworzono regał", shelf.getUuid()));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć regału o nazwie "+name));
        }
    }
    @Transactional
    public ResponseEntity<?> addSpot(HttpServletRequest httpRequest, StructureDTO structureDTO, UUID uuid) {
        String name = structureDTO.getName();
        Shelf shelf = shelfRepository.findShelfByUuid(uuid).orElse(null);
        if(shelf != null){
            Spot spot = new Spot();
            spot.setName(name);
            spot.setShelf(shelf);
            spot.set_free(true);
            spotRepository.saveAndFlush(spot);
            return ResponseEntity.ok().body(new WarehouseResponse("Utworzono miejsce magazynowe", spot.getUuid()));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało się utworzyć miejsca magazynowego o nazwie "+name));
        }
    }

    public ResponseEntity<?> deleteSpot(HttpServletRequest httpRequest, UUID uuid) {
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
        List<SpotDTO> locations = spotRepository.findAll().stream().map(
                e->new SpotDTO(
                        (e.getId())
                        ,(e.getUuid())
                        ,(e.getName())
                        ,(e.getShelf().getId())
                        ,(e.getShelf().getUuid())
                        ,(e.getShelf().getName())
                        ,(e.getShelf().getHall().getId())
                        ,(e.getShelf().getHall().getUuid())
                        ,(e.getShelf().getHall().getName())
                        ,(e.getShelf().getHall().getWarehouse().getId())
                        ,(e.getShelf().getHall().getWarehouse().getUuid())
                        ,(e.getShelf().getHall().getWarehouse().getName())
                        ,(e.is_free()))
        ).toList();

        return ResponseEntity.ok(locations);
    }

    public ResponseEntity<?> stateOfWarehouseByUUID(HttpServletRequest httpRequest, UUID uuid) {
        Warehouse warehouse = warehouseRepository.findWarehouseByUuid(uuid).orElse(null);
        if(warehouse==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
        StateOfWarehouse stateOfWarehouse = new StateOfWarehouse();
        List<SpotDTO> locations = spotRepository.findAll().stream().map(
                e->new SpotDTO(
                        (e.getId())
                        ,(e.getUuid())
                        ,(e.getName())
                        ,(e.getShelf().getId())
                        ,(e.getShelf().getUuid())
                        ,(e.getShelf().getName())
                        ,(e.getShelf().getHall().getId())
                        ,(e.getShelf().getHall().getUuid())
                        ,(e.getShelf().getHall().getName())
                        ,(e.getShelf().getHall().getWarehouse().getId())
                        ,(e.getShelf().getHall().getWarehouse().getUuid())
                        ,(e.getShelf().getHall().getWarehouse().getName())
                        ,(e.is_free()))
        ).toList();
        Map<Long, Long> mapaOgolem = new HashMap<>();
        Map<Long, Long> mapaWolnych = new HashMap<>();

        locations.forEach(e -> {
            mapaOgolem.merge(e.getHall(), 1L, Long::sum);
            if (e.is_free()) {
                mapaWolnych.merge(e.getHall(), 1L, Long::sum);
            }
        });

        stateOfWarehouse.setLocations(locations);
        stateOfWarehouse.setNumberAll(mapaOgolem);
        stateOfWarehouse.setNumberFree(mapaWolnych);
        if(locations!=null && locations.size()>0){
            return ResponseEntity.ok(stateOfWarehouse);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("Nie znaleziono magazynu o podanym UUID"));
        }
    }
    public ResponseEntity<?> getReportStateOfWarehouses(HttpServletRequest httpServletRequest) {
        Workbook workbook = new XSSFWorkbook();
        String time = new Timestamp(System.currentTimeMillis()).toString().replace(":","");
        Sheet sheet = workbook.createSheet("Stan magazynu "+time);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Warehouse ID");
        header.createCell(1).setCellValue("Warehouse UUID");
        header.createCell(2).setCellValue("Warehouse Name");
        header.createCell(3).setCellValue("Hall ID");
        header.createCell(4).setCellValue("Hall UUID");
        header.createCell(5).setCellValue("Hall Name");
        header.createCell(6).setCellValue("Shelf ID");
        header.createCell(7).setCellValue("Shelf UUID");
        header.createCell(8).setCellValue("Shelf Name");
        header.createCell(9).setCellValue("Spot ID");
        header.createCell(10).setCellValue("Spot UUID");
        header.createCell(11).setCellValue("Spot Name");
        header.createCell(12).setCellValue("Is Free");
        AtomicInteger counter = new AtomicInteger(0);
        spotRepository.selectStateOfWarehouse().stream().forEach(
                e->{
                    Row row = sheet.createRow(counter.incrementAndGet());
                    row.createCell(0).setCellValue(e.getWarehouse_id());
                    row.createCell(1).setCellValue(e.getWarehouse_uuid().toString());
                    row.createCell(2).setCellValue(e.getWarehouse_name());
                    row.createCell(3).setCellValue(e.getHall_id());
                    row.createCell(4).setCellValue(e.getHall_uuid().toString());
                    row.createCell(5).setCellValue(e.getHall_name());
                    row.createCell(6).setCellValue(e.getShelf_id());
                    row.createCell(7).setCellValue(e.getShelf_uuid().toString());
                    row.createCell(8).setCellValue(e.getShelf_name());
                    row.createCell(9).setCellValue(e.getSpot_id());
                    row.createCell(10).setCellValue(e.getSpot_uuid().toString());
                    row.createCell(11).setCellValue(e.getSpot_name());
                    row.createCell(12).setCellValue(e.getIs_free());
                }
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            workbook.write(outputStream);
            workbook.close();
        }
        catch (Exception e){
            System.out.println("TTT");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stanMagazynu.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }

    public ResponseEntity<?> getFreeSpots() {
        List<FreeSpots> freeSpots = spotRepository.findAllByFree(true).stream().map(
                e->new FreeSpots(e.getName()+" "+e.getShelf().getName()+" "+e.getShelf().getHall().getName(),e.getId())
        ).toList();
        return ResponseEntity.ok(freeSpots);
    }
}
