package com.pontus.crew.dto;

import com.pontus.crew.entity.CrewPosition;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrewMemberCreateRequest {
    
    @NotNull(message = "Vessel ID is required")
    private Long vesselId;
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality must not exceed 50 characters")
    private String nationality;
    
    @NotNull(message = "Position is required")
    private CrewPosition position;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Passport number is required")
    @Size(max = 50, message = "Passport number must not exceed 50 characters")
    private String passportNumber;
    
    @Size(max = 50, message = "Certificate must not exceed 50 characters")
    private String certificate;
    
    private LocalDate certificateExpiry;
}
