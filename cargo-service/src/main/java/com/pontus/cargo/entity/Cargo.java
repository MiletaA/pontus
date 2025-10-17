package com.pontus.cargo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cargo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Vessel ID is required")
    @Column(name = "vessel_id", nullable = false)
    private Long vesselId;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    
    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Weight must have at most 8 integer digits and 2 decimal places")
    @Column(name = "weight_tons", nullable = false, precision = 10, scale = 2)
    private BigDecimal weightTons;
    
    @NotNull(message = "Dangerous cargo flag is required")
    @Column(name = "is_dangerous", nullable = false)
    private Boolean isDangerous = false;
    
    @NotNull(message = "Customs status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "customs_status", nullable = false)
    private CustomsStatus customsStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type", length = 50)
    private CargoType cargoType;
    
    @Size(max = 100, message = "Origin must not exceed 100 characters")
    @Column(name = "origin", length = 100)
    private String origin;
    
    @Size(max = 100, message = "Destination must not exceed 100 characters")
    @Column(name = "destination", length = 100)
    private String destination;
    
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
