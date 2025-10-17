package com.pontus.crew.dto;

import com.pontus.crew.entity.CrewPosition;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CrewMemberResponse {
    
    private Long id;
    private Long vesselId;
    private String name;
    private String nationality;
    private CrewPosition position;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String certificate;
    private LocalDate certificateExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
