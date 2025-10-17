package com.pontus.vessel.service;

import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.exception.VesselOperationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for validating vessel-related data.
* Status Flow: SCHEDULED → UNDERWAY → ANCHORED → BERTHED → DEPARTED
* Alternative flows: DELAYED, CANCELLED can occur at any point
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class VesselValidationService {
    

    
    /**
     * Validates the consistency of arrival and departure dates.
     * 
     * Business Rules:
     * - Scheduled departure cannot be before scheduled arrival
     * - Actual departure cannot be before actual arrival
     * 
     * @param scheduledArrival The scheduled arrival date/time
     * @param scheduledDeparture The scheduled departure date/time
     * @param actualArrival The actual arrival date/time
     * @param actualDeparture The actual departure date/time
     * @throws InvalidVesselDataException if date validation fails
     */
    public void validateDates(LocalDateTime scheduledArrival, LocalDateTime scheduledDeparture,
                             LocalDateTime actualArrival, LocalDateTime actualDeparture) {
        validateScheduledDates(scheduledArrival, scheduledDeparture);
        validateActualDates(actualArrival, actualDeparture);
    }
    
    /**
     * Validates that the vessel status is one of the allowed values.
     * 
     * @param status The vessel status to validate
     * @throws VesselOperationException if status is invalid
     */
    public void validateStatus(VesselStatus status) {
        if (status == null) {
            throw VesselOperationException.invalidVesselStatus("null", "Status cannot be null");
        }
        // Note: VesselStatus enum provides type safety, so no additional validation needed
        // All enum values are valid by definition
    }
    
    /**
     * Validates that the IMO number follows the correct format.
     * IMO numbers must start with "IMO" followed by exactly 7 digits.
     * 
     * @param imoNumber The IMO number to validate
     * @throws VesselOperationException if IMO number format is invalid
     */
    public void validateImoNumberFormat(String imoNumber) {
        if (imoNumber == null || !imoNumber.matches("^IMO\\d{7}$")) {
            throw VesselOperationException.invalidImoNumberFormat(imoNumber, "IMO number must follow format 'IMO' followed by 7 digits (e.g., IMO1234567)");
        }
    }
    
    /**
     * Validates that arrival time is not after departure time for scheduled dates.
     * 
     * @param scheduledArrival The scheduled arrival time
     * @param scheduledDeparture The scheduled departure time
     * @throws VesselOperationException if scheduled departure is before arrival
     */
    private void validateScheduledDates(LocalDateTime scheduledArrival, LocalDateTime scheduledDeparture) {
        if (scheduledArrival != null && scheduledDeparture != null) {
            if (scheduledDeparture.isBefore(scheduledArrival)) {
                throw VesselOperationException.invalidDateRange(scheduledArrival, scheduledDeparture, "Scheduled departure cannot be before scheduled arrival");
            }
        }
    }
    
    /**
     * Validates that actual arrival time is not after actual departure time.
     * 
     * @param actualArrival The actual arrival time
     * @param actualDeparture The actual departure time
     * @throws VesselOperationException if actual departure is before arrival
     */
    private void validateActualDates(LocalDateTime actualArrival, LocalDateTime actualDeparture) {
        if (actualArrival != null && actualDeparture != null) {
            if (actualDeparture.isBefore(actualArrival)) {
                throw VesselOperationException.invalidDateRange(actualArrival, actualDeparture, "Actual departure cannot be before actual arrival");
            }
        }
    }
    
    /**
     * Gets the list of valid vessel statuses.
     * @return Array of VesselStatus enum values
     */
    public VesselStatus[] getValidStatuses() {
        return VesselStatus.values();
    }
}
