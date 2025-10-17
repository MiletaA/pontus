package com.pontus.cargo.dto;

import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CargoResponse {
    
    private Long id;
    private Long vesselId;
    private String description;
    private BigDecimal weightTons;
    private Boolean isDangerous;
    private CustomsStatus customsStatus;
    private CargoType cargoType;
    private String origin;
    private String destination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
