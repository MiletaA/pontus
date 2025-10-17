package com.pontus.delivery.dto;

import com.pontus.delivery.entity.DeliveryStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InlandDeliveryUpdateRequest {
    
    @NotNull(message = "Cargo ID is required")
    private Long cargoId;
    
    @NotBlank(message = "Destination address is required")
    @Size(max = 500, message = "Destination address must not exceed 500 characters")
    private String destinationAddress;
    
    @NotNull(message = "Delivery status is required")
    private DeliveryStatus deliveryStatus;
    
    @NotBlank(message = "Vehicle registration is required")
    @Size(max = 20, message = "Vehicle registration must not exceed 20 characters")
    private String vehicleRegistration;
    
    @NotBlank(message = "Driver name is required")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;
    
    private LocalDateTime deliveryTime;
    
    private LocalDateTime scheduledDeliveryTime;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
