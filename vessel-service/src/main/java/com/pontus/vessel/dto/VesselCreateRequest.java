package com.pontus.vessel.dto;

import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.enums.VesselType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VesselCreateRequest {
    
    @NotBlank(message = "Vessel name is required")
    @Size(max = 100, message = "Vessel name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "IMO number is required")
    @Size(max = 20, message = "IMO number must not exceed 20 characters")
    @Pattern(regexp = "^IMO\\d{7}$", message = "IMO number must follow format IMO followed by 7 digits")
    private String imoNumber;
    
    @NotNull(message = "Vessel type is required")
    private VesselType vesselType;
    
    @NotNull(message = "Vessel length is required")
    @DecimalMin(value = "0.01", message = "Vessel length must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Vessel length must have at most 8 integer digits and 2 decimal places")
    private BigDecimal length;
    
    @NotBlank(message = "Flag country is required")
    @Size(max = 50, message = "Flag country must not exceed 50 characters")
    private String flagCountry;
    
    @NotNull(message = "Status is required")
    private VesselStatus status;
    
    private LocalDateTime scheduledArrival;
    
    private LocalDateTime scheduledDeparture;
    
    private LocalDateTime actualArrival;
    
    private LocalDateTime actualDeparture;
}
