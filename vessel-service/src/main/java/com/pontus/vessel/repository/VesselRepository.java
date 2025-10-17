package com.pontus.vessel.repository;

import com.pontus.vessel.entity.Vessel;
import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.enums.VesselType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {
    
    Optional<Vessel> findByImoNumber(String imoNumber);
    
    Page<Vessel> findByStatus(VesselStatus status, Pageable pageable);
    
    Page<Vessel> findByVesselType(VesselType vesselType, Pageable pageable);
    
    Page<Vessel> findByFlagCountry(String flagCountry, Pageable pageable);
    
    @Query("SELECT v FROM Vessel v WHERE v.name LIKE %:name%")
    List<Vessel> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT v FROM Vessel v WHERE v.scheduledArrival IS NOT NULL AND v.actualArrival IS NULL")
    List<Vessel> findVesselsExpectedToArrive();
    
    @Query("SELECT v FROM Vessel v WHERE v.scheduledDeparture IS NOT NULL AND v.actualDeparture IS NULL")
    List<Vessel> findVesselsExpectedToDepart();
    
    @Query("SELECT v FROM Vessel v WHERE v.actualArrival IS NOT NULL AND v.actualDeparture IS NULL")
    List<Vessel> findVesselsCurrentlyInPort();
    
    boolean existsByImoNumber(String imoNumber);
}
