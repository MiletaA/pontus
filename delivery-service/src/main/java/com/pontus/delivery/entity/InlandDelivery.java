package com.pontus.delivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inland_delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlandDelivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Cargo ID is required")
    @Column(name = "cargo_id", nullable = false)
    private Long cargoId;
    
    @NotBlank(message = "Destination address is required")
    @Size(max = 500, message = "Destination address must not exceed 500 characters")
    @Column(name = "destination_address", nullable = false, length = 500)
    private String destinationAddress;
    
    @NotNull(message = "Delivery status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;
    
    @NotBlank(message = "Vehicle registration is required")
    @Size(max = 20, message = "Vehicle registration must not exceed 20 characters")
    @Column(name = "vehicle_registration", nullable = false, length = 20)
    private String vehicleRegistration;
    
    @NotBlank(message = "Driver name is required")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    @Column(name = "driver_name", nullable = false, length = 100)
    private String driverName;
    
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;
    
    @Column(name = "scheduled_delivery_time")
    private LocalDateTime scheduledDeliveryTime;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
