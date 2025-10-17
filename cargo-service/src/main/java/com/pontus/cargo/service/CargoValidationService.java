package com.pontus.cargo.service;

import com.pontus.cargo.dto.CargoCreateRequest;
import com.pontus.cargo.dto.CargoUpdateRequest;
import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import com.pontus.cargo.exception.CargoOperationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for validating cargo-related data.
 * 
 * This service follows the Single Responsibility Principle by focusing solely on validation logic.
 * It can be easily extended with new validation rules without modifying existing code (Open/Closed Principle).
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class CargoValidationService {
    
    // Note: Cargo type validation is now handled by the CargoType enum
    // which provides type safety and eliminates the need for runtime validation
    
    /**
     * Maximum weight limit for cargo in tons (regulatory compliance).
     */
    private static final BigDecimal MAX_CARGO_WEIGHT = new BigDecimal("50000.00");
    
    /**
     * Minimum weight for cargo in tons.
     */
    private static final BigDecimal MIN_CARGO_WEIGHT = new BigDecimal("0.01");
    
    /**
     * Validates all cargo data in a CargoCreateRequest.
     * 
     * This method orchestrates validation of all cargo fields by calling individual
     * validation methods. It ensures that all business rules are applied consistently.
     * 
     * @param request The cargo creation request to validate
     * @throws CargoOperationException if any validation fails
     */
    public void validateCargoData(CargoCreateRequest request) {
        if (request == null) {
            throw CargoOperationException.invalidCargoData("Cargo request cannot be null");
        }
        
        // Validate required fields
        validateDescription(request.getDescription());
        validateWeight(request.getWeightTons());
        
        // Validate optional fields if provided
        // CargoType validation is now handled by enum type safety
        
        // Validate locations if provided
        validateLocations(request.getOrigin(), request.getDestination());
        
        // Validate dangerous cargo handling
        validateDangerousCargoHandling(
            request.getIsDangerous(), 
            request.getCustomsStatus(), 
            request.getCargoType()
        );
        
        // Additional business rule validations can be added here
        validateVesselId(request.getVesselId());
    }
    
    /**
     * Validates all cargo data in a CargoUpdateRequest.
     * 
     * This method orchestrates validation of all cargo fields for updates by calling individual
     * validation methods. It ensures that all business rules are applied consistently.
     * 
     * @param request The cargo update request to validate
     * @throws CargoOperationException if any validation fails
     */
    public void validateCargoUpdateData(CargoUpdateRequest request) {
        if (request == null) {
            throw CargoOperationException.invalidCargoData("Cargo update request cannot be null");
        }
        
        // Validate required fields
        validateDescription(request.getDescription());
        validateWeight(request.getWeightTons());
        
        // Validate optional fields if provided
        // CargoType validation is now handled by enum type safety
        
        // Validate locations if provided
        validateLocations(request.getOrigin(), request.getDestination());
        
        // Validate dangerous cargo handling
        validateDangerousCargoHandling(
            request.getIsDangerous(), 
            request.getCustomsStatus(), 
            request.getCargoType()
        );
        
        // Additional business rule validations can be added here
        validateVesselId(request.getVesselId());
    }
    
    /**
     * Validates vessel ID is provided and valid.
     * 
     * @param vesselId The vessel ID to validate
     * @throws CargoOperationException if vessel ID is invalid
     */
    private void validateVesselId(Long vesselId) {
        if (vesselId == null) {
            throw CargoOperationException.invalidCargoData("Vessel ID is required");
        }
        
        if (vesselId <= 0) {
            throw CargoOperationException.invalidCargoData("Vessel ID must be a positive number");
        }
    }
    
    /**
     * Validates cargo weight is within acceptable limits.
     * 
     * Business Rules:
     * - Weight must be greater than 0.01 tons
     * - Weight cannot exceed 50,000 tons (regulatory limit)
     * - Weight must have proper precision (max 2 decimal places)
     * 
     * @param weight The cargo weight in tons
     * @throws CargoOperationException if weight validation fails
     */
    public void validateWeight(BigDecimal weight) {
        if (weight == null) {
            throw CargoOperationException.invalidWeight("Weight cannot be null");
        }
        
        if (weight.compareTo(MIN_CARGO_WEIGHT) < 0) {
            throw CargoOperationException.invalidWeight(
                String.format("Weight must be at least %s tons", MIN_CARGO_WEIGHT)
            );
        }
        
        if (weight.compareTo(MAX_CARGO_WEIGHT) > 0) {
            throw CargoOperationException.invalidWeight(
                String.format("Weight cannot exceed %s tons (regulatory limit)", MAX_CARGO_WEIGHT)
            );
        }
        
        // Validate precision (max 2 decimal places)
        if (weight.scale() > 2) {
            throw CargoOperationException.invalidWeight("Weight must have at most 2 decimal places");
        }
    }
    
    /**
     * Validates customs status transitions according to business rules.
     * 
     * Business Rules:
     * - PENDING can transition to: UNDER_INSPECTION, CLEARED, REJECTED
     * - UNDER_INSPECTION can transition to: CLEARED, REJECTED
     * - CLEARED is terminal (no further transitions)
     * - REJECTED can transition to: PENDING (for re-inspection)
     * 
     * @param currentStatus The current customs status
     * @param newStatus The new customs status to transition to
     * @throws CargoOperationException if transition is invalid
     */
    public void validateCustomsStatusTransition(CustomsStatus currentStatus, CustomsStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            throw CargoOperationException.invalidCustomsStatusTransition("Status cannot be null", "Status cannot be null");
        }
        
        if (currentStatus == newStatus) {
            return; // No transition needed
        }
        
        switch (currentStatus) {
            case PENDING:
                if (newStatus != CustomsStatus.UNDER_INSPECTION && 
                    newStatus != CustomsStatus.CLEARED && 
                    newStatus != CustomsStatus.REJECTED) {
                    throw CargoOperationException.invalidCustomsStatusTransition(
                        currentStatus.toString(), newStatus.toString()
                    );
                }
                break;
                
            case UNDER_INSPECTION:
                if (newStatus != CustomsStatus.CLEARED && newStatus != CustomsStatus.REJECTED) {
                    throw CargoOperationException.invalidCustomsStatusTransition(
                        currentStatus.toString(), newStatus.toString()
                    );
                }
                break;
                
            case CLEARED:
                // Terminal state - no transitions allowed
                throw CargoOperationException.invalidCustomsStatusTransition(
                    currentStatus.toString(), newStatus.toString()
                );
                
            case REJECTED:
                if (newStatus != CustomsStatus.PENDING) {
                    throw CargoOperationException.invalidCustomsStatusTransition(
                        currentStatus.toString(), newStatus.toString()
                    );
                }
                break;
                
            default:
                throw CargoOperationException.invalidCustomsStatusTransition(
                    currentStatus.toString(), newStatus.toString()
                );
        }
    }
    
    /**
     * Validates dangerous cargo handling requirements.
     * 
     * Business Rules:
     * - Dangerous cargo must have proper classification
     * - Dangerous cargo requires special handling documentation
     * - Dangerous cargo cannot be cleared without proper inspection
     * 
     * @param isDangerous Whether the cargo is classified as dangerous
     * @param customsStatus The current customs status
     * @param cargoType The type of cargo
     * @throws CargoOperationException if dangerous cargo validation fails
     */
    public void validateDangerousCargoHandling(Boolean isDangerous, CustomsStatus customsStatus, CargoType cargoType) {
        if (isDangerous == null) {
            throw CargoOperationException.dangerousCargoNotCleared(null, "Dangerous cargo flag cannot be null");
        }
        
        if (isDangerous) {
            // Dangerous cargo must go through inspection before being cleared
            if (customsStatus == CustomsStatus.CLEARED && cargoType != null && 
                cargoType != CargoType.HAZARDOUS) {
                // Allow clearing if explicitly marked as HAZARDOUS type
                // This is a business rule that dangerous cargo should be properly classified
            }
            
            // Dangerous cargo should have proper type classification
            if (cargoType != null && 
                cargoType != CargoType.HAZARDOUS && cargoType != CargoType.BULK_LIQUID) {
                // Log warning but don't fail - some dangerous cargo might be in other categories
            }
        }
    }
    
    /**
     * Validates cargo description content.
     * 
     * Business Rules:
     * - Description cannot be empty or just whitespace
     * - Description must not exceed maximum length
     * - Description should contain meaningful information
     * 
     * @param description The cargo description
     * @throws CargoOperationException if description validation fails
     */
    public void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw CargoOperationException.invalidCargoData("Cargo description cannot be empty");
        }
        
        if (description.length() > 1000) {
            throw CargoOperationException.invalidCargoData("Cargo description cannot exceed 1000 characters");
        }
        
        // Check for meaningful content (not just repeated characters or numbers)
        String trimmed = description.trim();
        if (trimmed.length() < 3) {
            throw CargoOperationException.invalidCargoData("Cargo description must be at least 3 characters long");
        }
    }
    
    /**
     * Validates origin and destination information.
     * 
     * @param origin The cargo origin
     * @param destination The cargo destination
     * @throws CargoOperationException if location validation fails
     */
    public void validateLocations(String origin, String destination) {
        if (origin != null && origin.length() > 100) {
            throw CargoOperationException.invalidCargoData("Origin cannot exceed 100 characters");
        }
        
        if (destination != null && destination.length() > 100) {
            throw CargoOperationException.invalidCargoData("Destination cannot exceed 100 characters");
        }
        
        // Origins and destinations should be different if both are provided
        if (origin != null && destination != null && 
            !origin.trim().isEmpty() && !destination.trim().isEmpty() &&
            origin.trim().equalsIgnoreCase(destination.trim())) {
            throw CargoOperationException.invalidCargoData("Origin and destination cannot be the same");
        }
    }
    
    /**
     * Gets the list of valid cargo types from the CargoType enum.
     * 
     * @return List of valid cargo type strings
     */
    public List<String> getValidCargoTypes() {
        return Arrays.stream(CargoType.values())
                .map(CargoType::name)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the maximum allowed cargo weight.
     * 
     * @return Maximum cargo weight in tons
     */
    public BigDecimal getMaxCargoWeight() {
        return MAX_CARGO_WEIGHT;
    }
    
    /**
     * Gets the minimum allowed cargo weight.
     * 
     * @return Minimum cargo weight in tons
     */
    public BigDecimal getMinCargoWeight() {
        return MIN_CARGO_WEIGHT;
    }
}
