package com.pontus.dock.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DockUpdateRequest {
    
    @NotBlank(message = "Dock name is required")
    @Size(max = 100, message = "Dock name must not exceed 100 characters")
    private String name;
    
    @NotNull(message = "Maximum length is required")
    @DecimalMin(value = "0.01", message = "Maximum length must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Maximum length must have at most 8 integer digits and 2 decimal places")
    private BigDecimal maxLength;
    
    @NotNull(message = "Handles dangerous cargo flag is required")
    private Boolean handlesDangerous;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
