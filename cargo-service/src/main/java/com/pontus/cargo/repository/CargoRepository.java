package com.pontus.cargo.repository;

import com.pontus.cargo.entity.Cargo;
import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    
    Page<Cargo> findByVesselId(Long vesselId, Pageable pageable);
    
    Page<Cargo> findByCustomsStatus(CustomsStatus customsStatus, Pageable pageable);
    
    Page<Cargo> findByIsDangerous(Boolean isDangerous, Pageable pageable);
    
    Page<Cargo> findByCargoType(CargoType cargoType, Pageable pageable);
    
    Page<Cargo> findByOrigin(String origin, Pageable pageable);
    
    Page<Cargo> findByDestination(String destination, Pageable pageable);
    
    @Query("SELECT c FROM Cargo c WHERE c.weightTons >= :minWeight")
    List<Cargo> findByWeightGreaterThanEqual(@Param("minWeight") BigDecimal minWeight);
    
    @Query("SELECT c FROM Cargo c WHERE c.weightTons <= :maxWeight")
    List<Cargo> findByWeightLessThanEqual(@Param("maxWeight") BigDecimal maxWeight);
    
    @Query("SELECT c FROM Cargo c WHERE c.weightTons BETWEEN :minWeight AND :maxWeight")
    List<Cargo> findByWeightBetween(@Param("minWeight") BigDecimal minWeight, @Param("maxWeight") BigDecimal maxWeight);
    
    @Query("SELECT c FROM Cargo c WHERE c.isDangerous = true AND c.customsStatus = 'CLEARED'")
    List<Cargo> findDangerousCargoCleared();
    
    @Query("SELECT c FROM Cargo c WHERE c.customsStatus = 'PENDING' ORDER BY c.createdAt ASC")
    List<Cargo> findPendingCargoOrderedByDate();
    
    @Query("SELECT c FROM Cargo c WHERE c.vesselId = :vesselId AND c.isDangerous = true")
    List<Cargo> findDangerousCargoByVessel(@Param("vesselId") Long vesselId);
    
    @Query("SELECT COUNT(c) FROM Cargo c WHERE c.customsStatus = :status")
    long countByCustomsStatus(@Param("status") CustomsStatus status);
    
    @Query("SELECT COUNT(c) FROM Cargo c WHERE c.isDangerous = true")
    long countDangerousCargo();
    
    @Query("SELECT SUM(c.weightTons) FROM Cargo c WHERE c.vesselId = :vesselId")
    BigDecimal getTotalWeightByVessel(@Param("vesselId") Long vesselId);
    
    @Query("SELECT SUM(c.weightTons) FROM Cargo c")
    BigDecimal getTotalWeight();
}
