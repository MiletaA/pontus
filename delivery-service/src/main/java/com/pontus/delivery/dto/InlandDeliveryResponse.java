package com.pontus.delivery.dto;

import com.pontus.delivery.entity.DeliveryStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InlandDeliveryResponse {
    
    private Long id;
    private Long cargoId;
    private String destinationAddress;
    private DeliveryStatus deliveryStatus;
    private String vehicleRegistration;
    private String driverName;
    private LocalDateTime deliveryTime;
    private LocalDateTime scheduledDeliveryTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
