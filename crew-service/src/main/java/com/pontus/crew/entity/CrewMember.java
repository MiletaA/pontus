package com.pontus.crew.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "crew_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrewMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Vessel ID is required")
    @Column(name = "vessel_id", nullable = false)
    private Long vesselId;
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality must not exceed 50 characters")
    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality;
    
    @NotNull(message = "Position is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, length = 50)
    private CrewPosition position;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Passport number is required")
    @Size(max = 50, message = "Passport number must not exceed 50 characters")
    @Column(name = "passport_number", nullable = false, length = 50)
    private String passportNumber;
    
    @Size(max = 50, message = "Certificate must not exceed 50 characters")
    @Column(name = "certificate", length = 50)
    private String certificate;
    
    @Column(name = "certificate_expiry")
    private LocalDate certificateExpiry;
    
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
