package com.pontus.delivery.exception;

import com.pontus.delivery.entity.DeliveryStatus;
import java.time.LocalDateTime;

public class DeliveryOperationException extends RuntimeException {
    
    public DeliveryOperationException(String message) {
        super(message);
    }
    
    public DeliveryOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static DeliveryOperationException invalidStatusTransition(DeliveryStatus from, DeliveryStatus to) {
        return new DeliveryOperationException("Invalid delivery status transition from " + from + " to " + to);
    }
    
    public static DeliveryOperationException deliveryNotFound(Long deliveryId) {
        return new DeliveryOperationException("Delivery not found with ID: " + deliveryId);
    }
    
    public static DeliveryOperationException deliveryAlreadyCompleted(Long deliveryId) {
        return new DeliveryOperationException("Delivery " + deliveryId + " is already completed and cannot be modified");
    }
    
    public static DeliveryOperationException deliveryOverdue(Long deliveryId, LocalDateTime scheduledTime) {
        return new DeliveryOperationException("Delivery " + deliveryId + " is overdue. Scheduled for: " + scheduledTime);
    }
    
    public static DeliveryOperationException invalidScheduledTime(LocalDateTime scheduledTime) {
        return new DeliveryOperationException("Scheduled delivery time cannot be in the past: " + scheduledTime);
    }
    
    public static DeliveryOperationException vehicleUnavailable(String vehicleRegistration, LocalDateTime requestedTime) {
        return new DeliveryOperationException("Vehicle " + vehicleRegistration + " is not available at " + requestedTime);
    }
    
    public static DeliveryOperationException driverUnavailable(String driverName, LocalDateTime requestedTime) {
        return new DeliveryOperationException("Driver " + driverName + " is not available at " + requestedTime);
    }
    
    public static DeliveryOperationException invalidStatusTransition(DeliveryStatus from, DeliveryStatus to, String message) {
        return new DeliveryOperationException("Invalid delivery status transition from " + from + " to " + to + ": " + message);
    }
    
    public static DeliveryOperationException invalidScheduledTime(LocalDateTime scheduledTime, String message) {
        return new DeliveryOperationException("Invalid scheduled time " + scheduledTime + ": " + message);
    }
    
    public static DeliveryOperationException invalidDeliveryTime(LocalDateTime deliveryTime, String message) {
        return new DeliveryOperationException("Invalid delivery time " + deliveryTime + ": " + message);
    }
    
    public static DeliveryOperationException invalidVehicleRegistration(String message) {
        return new DeliveryOperationException("Invalid vehicle registration: " + message);
    }
    
    public static DeliveryOperationException invalidDriverName(String message) {
        return new DeliveryOperationException("Invalid driver name: " + message);
    }
    
    public static DeliveryOperationException invalidDestinationAddress(String message) {
        return new DeliveryOperationException("Invalid destination address: " + message);
    }
    
    public static DeliveryOperationException invalidDeliveryData(String message) {
        return new DeliveryOperationException("Invalid delivery data: " + message);
    }
    
    public static DeliveryOperationException invalidVehicleType(String message) {
        return new DeliveryOperationException("Invalid vehicle type: " + message);
    }
    
    public static DeliveryOperationException incompatibleCargoVehicle(String message) {
        return new DeliveryOperationException("Incompatible cargo-vehicle combination: " + message);
    }
}
