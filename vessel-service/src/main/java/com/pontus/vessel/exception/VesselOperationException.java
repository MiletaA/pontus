package com.pontus.vessel.exception;

import java.time.LocalDate;

/**
 * Exception thrown when vessel operations fail due to business rule violations or validation errors.
 * 
 * This exception follows the factory method pattern to provide consistent, descriptive error messages
 * for various vessel operation scenarios.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public class VesselOperationException extends RuntimeException {
    
    public VesselOperationException(String message) {
        super(message);
    }
    
    public VesselOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Factory methods for specific vessel operation exceptions
    
    public static VesselOperationException vesselNotFound(Long vesselId) {
        return new VesselOperationException("Vessel not found with ID: " + vesselId);
    }
    
    public static VesselOperationException vesselNotFound(String imoNumber) {
        return new VesselOperationException("Vessel not found with IMO number: " + imoNumber);
    }
    
    public static VesselOperationException duplicateImoNumber(String imoNumber) {
        return new VesselOperationException("Vessel with IMO number " + imoNumber + " already exists");
    }
    
    public static VesselOperationException invalidImoNumber(String imoNumber, String reason) {
        return new VesselOperationException("Invalid IMO number '" + imoNumber + "': " + reason);
    }
    
    public static VesselOperationException invalidVesselData(String field, String value, String reason) {
        return new VesselOperationException("Invalid vessel " + field + " '" + value + "': " + reason);
    }
    
    public static VesselOperationException invalidVesselData(String message) {
        return new VesselOperationException("Invalid vessel data: " + message);
    }
    
    public static VesselOperationException invalidVesselName(String name, String reason) {
        return new VesselOperationException("Invalid vessel name '" + name + "': " + reason);
    }
    
    public static VesselOperationException invalidVesselLength(Double length, String reason) {
        return new VesselOperationException("Invalid vessel length " + length + "m: " + reason);
    }
    
    public static VesselOperationException invalidVesselCapacity(Integer capacity, String reason) {
        return new VesselOperationException("Invalid vessel capacity " + capacity + ": " + reason);
    }
    
    public static VesselOperationException invalidVesselType(String vesselType, String reason) {
        return new VesselOperationException("Invalid vessel type '" + vesselType + "': " + reason);
    }
    
    public static VesselOperationException invalidDateRange(LocalDate startDate, LocalDate endDate, String reason) {
        return new VesselOperationException("Invalid date range from " + startDate + " to " + endDate + ": " + reason);
    }
    
    public static VesselOperationException invalidDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String reason) {
        return new VesselOperationException("Invalid date range from " + startDate + " to " + endDate + ": " + reason);
    }
    
    public static VesselOperationException invalidImoNumberFormat(String imoNumber, String reason) {
        return new VesselOperationException("Invalid IMO number format '" + imoNumber + "': " + reason);
    }
    
    public static VesselOperationException invalidVesselStatus(String status, String reason) {
        return new VesselOperationException("Invalid vessel status '" + status + "': " + reason);
    }
    
    public static VesselOperationException invalidVesselStatus(String currentStatus, String newStatus, String reason) {
        return new VesselOperationException("Invalid vessel status transition from " + currentStatus + " to " + newStatus + ": " + reason);
    }
    
    public static VesselOperationException vesselOperationFailed(String operation, String reason) {
        return new VesselOperationException("Vessel operation '" + operation + "' failed: " + reason);
    }
}
