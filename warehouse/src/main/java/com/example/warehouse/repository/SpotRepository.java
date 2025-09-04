package com.example.warehouse.repository;

import com.example.warehouse.entity.ReportDTO;
import com.example.warehouse.entity.Shelf;
import com.example.warehouse.entity.Spot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot,Long> {
    Optional<Spot> findSpotByUuid(UUID uuid);
    Optional<Spot> findSpotById(long id);
    @Query(
            nativeQuery = true,
            value = "SELECT SP.id,SP.uuid,SP.name, SP.id_shelf, SP.is_free FROM SPOTS SP " +
                    "JOIN SHELVES S ON S.id = SP.id_shelf " +
                    "JOIN HALLS H ON H.id = S.id_hall " +
                    "JOIN WAREHOUSES W ON W.id = H.id_warehouse " +
                    "WHERE W.uuid = :uuid"
    )
    Optional<List<Spot>> findByWarehouse(@Param("uuid") UUID uuid);
    List<Spot> findAllByShelf(Shelf shelf);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE spots SET is_free = :state WHERE id = :id")
    void changeState(@Param("state") boolean state, @Param("id") long id);

    @Query("""
    SELECT new com.example.warehouse.entity.ReportDTO(
        w.id, w.uuid, w.name,
        h.id, h.uuid, h.name,
        sh.id, sh.uuid, sh.name,
        s.id, s.uuid, s.name,
        s.is_free
    )
    FROM Spot s
    JOIN s.shelf sh
    JOIN sh.hall h
    JOIN h.warehouse w
    """)
    List<ReportDTO> selectStateOfWarehouse();
}
