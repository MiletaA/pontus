package com.pontus.dock.repository;

import com.pontus.dock.entity.Dock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DockRepository extends JpaRepository<Dock, Long> {
    
    Optional<Dock> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Dock> findByIsOccupied(Boolean isOccupied);
    
    List<Dock> findByHandlesDangerous(Boolean handlesDangerous);
    
    List<Dock> findByAssignedVesselId(Long vesselId);
    
    @Query("SELECT d FROM Dock d WHERE d.maxLength >= :minLength")
    List<Dock> findByMaxLengthGreaterThanEqual(@Param("minLength") BigDecimal minLength);
    
    @Query("SELECT d FROM Dock d WHERE d.isOccupied = false AND d.maxLength >= :vesselLength")
    List<Dock> findAvailableDocksForVessel(@Param("vesselLength") BigDecimal vesselLength);
    
    @Query("SELECT d FROM Dock d WHERE d.isOccupied = false AND d.handlesDangerous = true")
    List<Dock> findAvailableDangerousCargoCapableDocks();
    
    @Query("SELECT d FROM Dock d WHERE d.scheduledFrom IS NOT NULL AND d.scheduledTo IS NOT NULL " +
           "AND d.scheduledFrom <= :currentTime AND d.scheduledTo >= :currentTime")
    List<Dock> findDocksScheduledAt(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(d) FROM Dock d WHERE d.isOccupied = true")
    long countOccupiedDocks();
    
    @Query("SELECT COUNT(d) FROM Dock d WHERE d.isOccupied = false")
    long countAvailableDocks();
    
    @Query("SELECT COUNT(d) FROM Dock d WHERE d.handlesDangerous = true")
    long countDangerousCargoCapableDocks();
}
