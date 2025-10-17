package com.pontus.dock.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Dock name is required")
    @Size(max = 100, message = "Dock name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotNull(message = "Maximum length is required")
    @DecimalMin(value = "0.01", message = "Maximum length must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Maximum length must have at most 8 integer digits and 2 decimal places")
    @Column(name = "max_length", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxLength;
    
    @NotNull(message = "Occupied status is required")
    @Column(name = "is_occupied", nullable = false)
    private Boolean isOccupied = false;
    
    @Column(name = "assigned_vessel_id")
    private Long assignedVesselId;
    
    @Column(name = "scheduled_from")
    private LocalDateTime scheduledFrom;
    
    @Column(name = "scheduled_to")
    private LocalDateTime scheduledTo;
    
    @NotNull(message = "Handles dangerous cargo flag is required")
    @Column(name = "handles_dangerous", nullable = false)
    private Boolean handlesDangerous = false;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;
    
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
