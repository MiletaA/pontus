package com.pontus.cargo.dto;

import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CargoUpdateRequest {
    
    @NotNull(message = "Vessel ID is required")
    private Long vesselId;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Weight must have at most 8 integer digits and 2 decimal places")
    private BigDecimal weightTons;
    
    @NotNull(message = "Dangerous cargo flag is required")
    private Boolean isDangerous;
    
    @NotNull(message = "Customs status is required")
    private CustomsStatus customsStatus;
    
    private CargoType cargoType;
    
    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;
    
    @Size(max = 100, message = "Destination must not exceed 100 characters")
    private String destination;
}
