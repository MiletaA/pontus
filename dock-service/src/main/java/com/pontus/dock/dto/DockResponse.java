package com.pontus.dock.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DockResponse {
    
    private Long id;
    private String name;
    private BigDecimal maxLength;
    private Boolean isOccupied;
    private Long assignedVesselId;
    private LocalDateTime scheduledFrom;
    private LocalDateTime scheduledTo;
    private Boolean handlesDangerous;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
