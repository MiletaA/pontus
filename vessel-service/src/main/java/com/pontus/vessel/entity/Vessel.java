package com.pontus.vessel.entity;

import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.enums.VesselType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vessel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vessel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Vessel name is required")
    @Size(max = 100, message = "Vessel name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "IMO number is required")
    @Size(max = 20, message = "IMO number must not exceed 20 characters")
    @Pattern(regexp = "^IMO\\d{7}$", message = "IMO number must follow format IMO followed by 7 digits")
    @Column(name = "imo_number", nullable = false, length = 20, unique = true)
    private String imoNumber;
    
    @NotNull(message = "Vessel type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "vessel_type", nullable = false, length = 50)
    private VesselType vesselType;
    
    @NotNull(message = "Vessel length is required")
    @DecimalMin(value = "0.01", message = "Vessel length must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Vessel length must have at most 8 integer digits and 2 decimal places")
    @Column(name = "length", nullable = false, precision = 10, scale = 2)
    private BigDecimal length;
    
    @NotBlank(message = "Flag country is required")
    @Size(max = 50, message = "Flag country must not exceed 50 characters")
    @Column(name = "flag_country", nullable = false, length = 50)
    private String flagCountry;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private VesselStatus status;
    
    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;
    
    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;
    
    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;
    
    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;
    
    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (scheduledArrival != null && scheduledDeparture != null) {
            if (scheduledDeparture.isBefore(scheduledArrival)) {
                throw new IllegalArgumentException("Scheduled departure cannot be before scheduled arrival");
            }
        }
        
        if (actualArrival != null && actualDeparture != null) {
            if (actualDeparture.isBefore(actualArrival)) {
                throw new IllegalArgumentException("Actual departure cannot be before actual arrival");
            }
        }
    }
}
