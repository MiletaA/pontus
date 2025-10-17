package com.pontus.delivery.repository;

import com.pontus.delivery.entity.DeliveryStatus;
import com.pontus.delivery.entity.InlandDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InlandDeliveryRepository extends JpaRepository<InlandDelivery, Long> {
    
    List<InlandDelivery> findByCargoId(Long cargoId);
    
    List<InlandDelivery> findByDeliveryStatus(DeliveryStatus status);
    
    List<InlandDelivery> findByVehicleRegistration(String vehicleRegistration);
    
    List<InlandDelivery> findByDriverName(String driverName);
    
    Optional<InlandDelivery> findByCargoIdAndDeliveryStatus(Long cargoId, DeliveryStatus status);
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.scheduledDeliveryTime BETWEEN :startTime AND :endTime")
    List<InlandDelivery> findByScheduledDeliveryTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                                           @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.deliveryTime BETWEEN :startTime AND :endTime")
    List<InlandDelivery> findByDeliveryTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                                  @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.scheduledDeliveryTime < :currentTime AND d.deliveryStatus IN ('SCHEDULED', 'IN_TRANSIT')")
    List<InlandDelivery> findOverdueDeliveries(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.scheduledDeliveryTime BETWEEN :currentTime AND :endTime AND d.deliveryStatus = 'SCHEDULED'")
    List<InlandDelivery> findUpcomingDeliveries(@Param("currentTime") LocalDateTime currentTime, 
                                               @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.deliveryStatus = 'DELIVERED' AND d.deliveryTime IS NOT NULL")
    List<InlandDelivery> findCompletedDeliveries();
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.deliveryStatus IN ('FAILED', 'CANCELLED')")
    List<InlandDelivery> findFailedDeliveries();
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.destinationAddress LIKE %:address%")
    List<InlandDelivery> findByDestinationAddressContaining(@Param("address") String address);
    
    @Query("SELECT COUNT(d) FROM InlandDelivery d WHERE d.deliveryStatus = :status")
    long countByDeliveryStatus(@Param("status") DeliveryStatus status);
    
    @Query("SELECT COUNT(d) FROM InlandDelivery d WHERE d.driverName = :driverName")
    long countByDriverName(@Param("driverName") String driverName);
    
    @Query("SELECT COUNT(d) FROM InlandDelivery d WHERE d.vehicleRegistration = :vehicleRegistration")
    long countByVehicleRegistration(@Param("vehicleRegistration") String vehicleRegistration);
    
    @Query("SELECT DISTINCT d.driverName FROM InlandDelivery d ORDER BY d.driverName")
    List<String> findDistinctDriverNames();
    
    @Query("SELECT DISTINCT d.vehicleRegistration FROM InlandDelivery d ORDER BY d.vehicleRegistration")
    List<String> findDistinctVehicleRegistrations();
    
    @Query("SELECT d FROM InlandDelivery d WHERE d.deliveryStatus = 'IN_TRANSIT' ORDER BY d.scheduledDeliveryTime")
    List<InlandDelivery> findActiveDeliveries();
    
    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, d.scheduledDeliveryTime, d.deliveryTime)) FROM InlandDelivery d WHERE d.deliveryStatus = 'DELIVERED' AND d.deliveryTime IS NOT NULL")
    Double getAverageDeliveryDelayHours();
}
