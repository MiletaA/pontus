package com.pontus.delivery.service;

import com.pontus.delivery.entity.DeliveryStatus;
import com.pontus.delivery.exception.DeliveryOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DeliveryValidationService {
    
    private static final Pattern VEHICLE_REGISTRATION_PATTERN = Pattern.compile("^[A-Z]{2}\\d{3}[A-Z]{2}$");
    private static final Pattern DRIVER_NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final int MAX_NOTES_LENGTH = 500;
    private static final int MAX_ADDRESS_LENGTH = 200;
    
    /**
     * Validates scheduled delivery time
     */
    public void validateScheduledDeliveryTime(LocalDateTime scheduledTime) {
        log.debug("Validating scheduled delivery time: {}", scheduledTime);
        
        if (scheduledTime == null) {
            throw DeliveryOperationException.invalidScheduledTime(scheduledTime, "Scheduled delivery time cannot be null");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (scheduledTime.isBefore(now)) {
            throw DeliveryOperationException.invalidScheduledTime(scheduledTime);
        }
        
        // Check if scheduled time is too far in the future (e.g., more than 1 year)
        LocalDateTime maxFutureTime = now.plusYears(1);
        if (scheduledTime.isAfter(maxFutureTime)) {
            throw DeliveryOperationException.invalidScheduledTime(scheduledTime, "Scheduled time cannot be more than 1 year in the future");
        }
        
        log.debug("Scheduled delivery time validation passed");
    }
    
    /**
     * Validates vehicle registration format
     */
    public void validateVehicleRegistration(String vehicleRegistration) {
        log.debug("Validating vehicle registration: {}", vehicleRegistration);
        
        if (!StringUtils.hasText(vehicleRegistration)) {
            throw DeliveryOperationException.invalidVehicleRegistration("Vehicle registration cannot be null or empty");
        }
        
        if (!VEHICLE_REGISTRATION_PATTERN.matcher(vehicleRegistration.trim()).matches()) {
            throw DeliveryOperationException.invalidVehicleRegistration("Invalid format. Expected format: XX123XX");
        }
        
        log.debug("Vehicle registration validation passed");
    }
    
    /**
     * Validates driver name
     */
    public void validateDriverName(String driverName) {
        log.debug("Validating driver name: {}", driverName);
        
        if (!StringUtils.hasText(driverName)) {
            throw DeliveryOperationException.invalidDriverName("Driver name cannot be null or empty");
        }
        
        String trimmedName = driverName.trim();
        if (!DRIVER_NAME_PATTERN.matcher(trimmedName).matches()) {
            throw DeliveryOperationException.invalidDriverName("Name must contain only letters and spaces, 2-50 characters long");
        }
        
        log.debug("Driver name validation passed");
    }
    
    /**
     * Validates destination address
     */
    public void validateDestinationAddress(String destinationAddress) {
        log.debug("Validating destination address: {}", destinationAddress);
        
        if (!StringUtils.hasText(destinationAddress)) {
            throw DeliveryOperationException.invalidDestinationAddress("Destination address cannot be null or empty");
        }
        
        String trimmedAddress = destinationAddress.trim();
        if (trimmedAddress.length() > MAX_ADDRESS_LENGTH) {
            throw DeliveryOperationException.invalidDestinationAddress("Cannot exceed " + MAX_ADDRESS_LENGTH + " characters");
        }
        
        if (trimmedAddress.length() < 5) {
            throw DeliveryOperationException.invalidDestinationAddress("Must be at least 5 characters long");
        }
        
        log.debug("Destination address validation passed");
    }
    
    /**
     * Validates delivery notes
     */
    public void validateNotes(String notes) {
        log.debug("Validating delivery notes");
        
        if (notes != null && notes.trim().length() > MAX_NOTES_LENGTH) {
            throw DeliveryOperationException.invalidDeliveryData("Notes cannot exceed " + MAX_NOTES_LENGTH + " characters");
        }
        
        log.debug("Delivery notes validation passed");
    }
    
    /**
     * Validates status transition
     */
    public void validateStatusTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        log.debug("Validating status transition from {} to {}", currentStatus, newStatus);
        
        if (currentStatus == null || newStatus == null) {
            throw DeliveryOperationException.invalidDeliveryData("Current status and new status cannot be null");
        }
        
        if (currentStatus == newStatus) {
            throw DeliveryOperationException.invalidDeliveryData("New status must be different from current status");
        }
        
        // Define valid transitions
        boolean isValidTransition = switch (currentStatus) {
            case SCHEDULED -> newStatus == DeliveryStatus.IN_TRANSIT || newStatus == DeliveryStatus.CANCELLED;
            case IN_TRANSIT -> newStatus == DeliveryStatus.DELIVERED || newStatus == DeliveryStatus.FAILED;
            case DELIVERED -> false; // No transitions allowed from DELIVERED
            case FAILED -> newStatus == DeliveryStatus.SCHEDULED; // Allow rescheduling failed deliveries
            case CANCELLED -> newStatus == DeliveryStatus.SCHEDULED; // Allow rescheduling cancelled deliveries
        };
        
        if (!isValidTransition) {
            throw DeliveryOperationException.invalidStatusTransition(currentStatus, newStatus);
        }
        
        log.debug("Status transition validation passed");
    }
    
    /**
     * Validates actual delivery time against scheduled time
     */
    public void validateActualDeliveryTime(LocalDateTime actualTime, LocalDateTime scheduledTime) {
        log.debug("Validating actual delivery time: {} against scheduled time: {}", actualTime, scheduledTime);
        
        if (actualTime == null) {
            throw DeliveryOperationException.invalidDeliveryTime(null, "Actual delivery time cannot be null");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (actualTime.isAfter(now)) {
            throw DeliveryOperationException.invalidDeliveryTime(actualTime, "Actual delivery time cannot be in the future");
        }
        
        if (scheduledTime != null && actualTime.isBefore(scheduledTime.minusHours(24))) {
            throw DeliveryOperationException.invalidDeliveryTime(actualTime, "Actual delivery time cannot be more than 24 hours before scheduled time");
        }
        
        log.debug("Actual delivery time validation passed");
    }
    
    /**
     * Validates if delivery can be modified (not completed or cancelled)
     */
    public void validateDeliveryCanBeModified(Long deliveryId, DeliveryStatus currentStatus) {
        log.debug("Validating if delivery {} can be modified, current status: {}", deliveryId, currentStatus);
        
        if (currentStatus == DeliveryStatus.DELIVERED) {
            throw DeliveryOperationException.deliveryAlreadyCompleted(deliveryId);
        }
        
        log.debug("Delivery modification validation passed");
    }
    
    /**
     * Validates if delivery is overdue
     */
    public void validateDeliveryNotOverdue(Long deliveryId, LocalDateTime scheduledTime) {
        log.debug("Validating delivery {} is not overdue, scheduled time: {}", deliveryId, scheduledTime);
        
        if (scheduledTime != null && scheduledTime.isBefore(LocalDateTime.now())) {
            throw DeliveryOperationException.deliveryOverdue(deliveryId, scheduledTime);
        }
        
        log.debug("Delivery overdue validation passed");
    }
}