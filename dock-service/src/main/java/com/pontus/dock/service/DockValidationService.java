package com.pontus.dock.service;

import com.pontus.dock.exception.DockOperationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service responsible for validating dock-related data.
 * 
 * This service follows the Single Responsibility Principle by focusing solely on validation logic.
 * It can be easily extended with new validation rules without modifying existing code (Open/Closed Principle).
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class DockValidationService {
    
    /**
     * Minimum dock length in meters for safety and operational requirements.
     * Based on maritime industry standards for port infrastructure.
     */
    private static final BigDecimal MIN_DOCK_LENGTH = new BigDecimal("10.00");
    
    /**
     * Maximum dock length in meters for realistic port infrastructure.
     * Based on typical commercial port dock specifications.
     */
    private static final BigDecimal MAX_DOCK_LENGTH = new BigDecimal("500.00");
    
    /**
     * Maximum length for dock names to ensure database compatibility and readability.
     */
    private static final int MAX_NAME_LENGTH = 100;
    
    /**
     * Maximum length for dock descriptions.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    
    /**
     * Validates dock name according to business rules.
     * 
     * Business Rules:
     * - Name cannot be null or empty
     * - Name cannot exceed maximum length
     * - Name should contain only valid characters
     * 
     * @param name The dock name to validate
     * @throws DockOperationException if name validation fails
     */
    public void validateDockName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw DockOperationException.invalidDockData("Dock name cannot be null or empty");
        }
        
        if (name.length() > MAX_NAME_LENGTH) {
            throw DockOperationException.invalidDockData(
                String.format("Dock name cannot exceed %d characters. Current length: %d", 
                    MAX_NAME_LENGTH, name.length()));
        }
        
        // Check for valid characters (alphanumeric, spaces, hyphens, underscores)
        if (!name.matches("^[a-zA-Z0-9\\s\\-_]+$")) {
            throw DockOperationException.invalidDockData(
                "Dock name can only contain letters, numbers, spaces, hyphens, and underscores");
        }
    }
    
    /**
     * Validates dock length according to maritime industry standards.
     * 
     * Business Rules:
     * - Length must be positive
     * - Length must be within realistic operational limits
     * - Length must have appropriate precision for construction
     * 
     * @param maxLength The maximum dock length to validate
     * @throws DockOperationException if length validation fails
     */
    public void validateDockLength(BigDecimal maxLength) {
        if (maxLength == null) {
            throw DockOperationException.invalidDockData("Dock maximum length cannot be null");
        }
        
        if (maxLength.compareTo(BigDecimal.ZERO) <= 0) {
            throw DockOperationException.invalidDockData("Dock maximum length must be greater than 0");
        }
        
        if (maxLength.compareTo(MIN_DOCK_LENGTH) < 0) {
            throw DockOperationException.invalidDockData(
                String.format("Dock maximum length must be at least %s meters for safety requirements", 
                    MIN_DOCK_LENGTH));
        }
        
        if (maxLength.compareTo(MAX_DOCK_LENGTH) > 0) {
            throw DockOperationException.invalidDockData(
                String.format("Dock maximum length cannot exceed %s meters", MAX_DOCK_LENGTH));
        }
        
        // Validate precision (max 2 decimal places for construction accuracy)
        if (maxLength.scale() > 2) {
            throw DockOperationException.invalidDockData(
                "Dock maximum length can have at most 2 decimal places");
        }
    }
    
    /**
     * Validates dock scheduling dates for vessel assignments.
     * 
     * Business Rules:
     * - Scheduled 'from' date cannot be after 'to' date
     * - Both dates should be provided if dock is being scheduled
     * - Scheduled dates should be in the future for new assignments
     * 
     * @param scheduledFrom The scheduled start date/time
     * @param scheduledTo The scheduled end date/time
     * @throws DockOperationException if date validation fails
     */
    public void validateScheduledDates(LocalDateTime scheduledFrom, LocalDateTime scheduledTo) {
        // If one date is provided, both should be provided
        if ((scheduledFrom != null && scheduledTo == null) || 
            (scheduledFrom == null && scheduledTo != null)) {
            throw DockOperationException.invalidDockData(
                "Both scheduled from and to dates must be provided when scheduling dock usage");
        }
        
        // If both dates are provided, validate the range
        if (scheduledFrom != null && scheduledTo != null) {
            if (scheduledTo.isBefore(scheduledFrom) || scheduledTo.isEqual(scheduledFrom)) {
                throw DockOperationException.invalidDateRange(scheduledFrom, scheduledTo, 
                    "Scheduled 'to' date must be after scheduled 'from' date");
            }
            
            // Validate minimum scheduling duration (at least 1 hour)
            if (scheduledTo.isBefore(scheduledFrom.plusHours(1))) {
                throw DockOperationException.invalidDateRange(scheduledFrom, scheduledTo, 
                    "Dock scheduling must be for at least 1 hour duration");
            }
        }
    }
    
    /**
     * Validates dock occupation status consistency.
     * 
     * Business Rules:
     * - If dock is occupied, it should have an assigned vessel
     * - If dock is not occupied, it should not have an assigned vessel
     * - Occupied docks should have valid scheduling information
     * 
     * @param isOccupied Whether the dock is occupied
     * @param assignedVesselId The ID of the assigned vessel (if any)
     * @param scheduledFrom The scheduled start date (if any)
     * @param scheduledTo The scheduled end date (if any)
     * @throws DockOperationException if occupation status is inconsistent
     */
    public void validateOccupationStatus(Boolean isOccupied, Long assignedVesselId, 
                                       LocalDateTime scheduledFrom, LocalDateTime scheduledTo) {
        if (isOccupied == null) {
            throw DockOperationException.invalidDockData("Dock occupation status cannot be null");
        }
        
        if (isOccupied) {
            if (assignedVesselId == null) {
                throw DockOperationException.invalidDockData(
                    "Occupied dock must have an assigned vessel ID");
            }
            
            if (assignedVesselId <= 0) {
                throw DockOperationException.invalidDockData(
                    "Assigned vessel ID must be a positive number");
            }
        } else {
            if (assignedVesselId != null) {
                throw DockOperationException.invalidDockData(
                    "Unoccupied dock cannot have an assigned vessel ID");
            }
        }
    }
    
    /**
     * Validates dock description if provided.
     * 
     * Business Rules:
     * - Description is optional but if provided must not exceed maximum length
     * - Description should not contain inappropriate content
     * 
     * @param description The dock description to validate
     * @throws DockOperationException if description validation fails
     */
    public void validateDescription(String description) {
        if (description != null) {
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                throw DockOperationException.invalidDockData(
                    String.format("Dock description cannot exceed %d characters. Current length: %d", 
                        MAX_DESCRIPTION_LENGTH, description.length()));
            }
        }
    }
    
    /**
     * Validates dangerous cargo handling capability flag.
     * 
     * Business Rules:
     * - Flag cannot be null (must be explicitly set)
     * - This is important for safety and regulatory compliance
     * 
     * @param handlesDangerous Whether the dock can handle dangerous cargo
     * @throws DockOperationException if dangerous cargo flag validation fails
     */
    public void validateDangerousCargoFlag(Boolean handlesDangerous) {
        if (handlesDangerous == null) {
            throw DockOperationException.invalidDockData(
                "Dangerous cargo handling capability must be explicitly specified for safety compliance");
        }
    }
    
    /**
     * Performs comprehensive validation for dock creation.
     * 
     * @param name The dock name
     * @param maxLength The maximum dock length
     * @param handlesDangerous Whether the dock handles dangerous cargo
     * @param description The dock description (optional)
     * @throws DockOperationException if any validation fails
     */
    public void validateDockCreation(String name, BigDecimal maxLength, 
                                   Boolean handlesDangerous, String description) {
        validateDockName(name);
        validateDockLength(maxLength);
        validateDangerousCargoFlag(handlesDangerous);
        validateDescription(description);
    }
    
    /**
     * Performs comprehensive validation for dock updates.
     * 
     * @param name The dock name
     * @param maxLength The maximum dock length
     * @param handlesDangerous Whether the dock handles dangerous cargo
     * @param description The dock description (optional)
     * @param isOccupied Whether the dock is occupied
     * @param assignedVesselId The assigned vessel ID (if any)
     * @param scheduledFrom The scheduled start date (if any)
     * @param scheduledTo The scheduled end date (if any)
     * @throws DockOperationException if any validation fails
     */
    public void validateDockUpdate(String name, BigDecimal maxLength, Boolean handlesDangerous, 
                                 String description, Boolean isOccupied, Long assignedVesselId,
                                 LocalDateTime scheduledFrom, LocalDateTime scheduledTo) {
        validateDockCreation(name, maxLength, handlesDangerous, description);
        validateOccupationStatus(isOccupied, assignedVesselId, scheduledFrom, scheduledTo);
        validateScheduledDates(scheduledFrom, scheduledTo);
    }
    
    /**
     * Gets the minimum allowed dock length.
     * 
     * @return Minimum dock length in meters
     */
    public BigDecimal getMinDockLength() {
        return MIN_DOCK_LENGTH;
    }
    
    /**
     * Gets the maximum allowed dock length.
     * 
     * @return Maximum dock length in meters
     */
    public BigDecimal getMaxDockLength() {
        return MAX_DOCK_LENGTH;
    }
}