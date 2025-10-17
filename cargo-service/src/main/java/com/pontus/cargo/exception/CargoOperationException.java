package com.pontus.cargo.exception;

/**
 * Unified exception class for all cargo-related business operations.
 * 
 * This exception replaces multiple specific exceptions (CargoNotFoundException, etc.)
 * with a single, flexible exception that uses descriptive messages to indicate
 * the specific type of error that occurred.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public class CargoOperationException extends RuntimeException {

    /**
     * Constructs a new CargoOperationException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public CargoOperationException(String message) {
        super(message);
    }

    /**
     * Constructs a new CargoOperationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the cause of the exception
     * @param cause the underlying cause of the exception
     */
    public CargoOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Factory methods for common cargo operation errors

    /**
     * Creates an exception for when a cargo item is not found.
     *
     * @param cargoId the ID of the cargo that was not found
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException cargoNotFound(Long cargoId) {
        return new CargoOperationException(String.format("Cargo with ID %d not found", cargoId));
    }

    /**
     * Creates an exception for when a cargo item already exists.
     *
     * @param identifier the identifier of the cargo that already exists
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException cargoAlreadyExists(String identifier) {
        return new CargoOperationException(String.format("Cargo with identifier '%s' already exists", identifier));
    }

    /**
     * Creates an exception for invalid cargo data.
     *
     * @param message the specific validation error message
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException invalidCargoData(String message) {
        return new CargoOperationException("Invalid cargo data: " + message);
    }

    /**
     * Creates an exception for invalid customs status transitions.
     *
     * @param currentStatus the current customs status
     * @param targetStatus the attempted target status
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException invalidStatusTransition(String currentStatus, String targetStatus) {
        return new CargoOperationException(
            String.format("Invalid customs status transition from %s to %s", currentStatus, targetStatus));
    }

    /**
     * Creates an exception for invalid weight values.
     *
     * @param message the specific weight validation error message
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException invalidWeight(String message) {
        return new CargoOperationException("Invalid weight: " + message);
    }

    /**
     * Creates an exception for invalid cargo type.
     *
     * @param cargoType the invalid cargo type
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException invalidCargoType(String cargoType) {
        return new CargoOperationException("Invalid cargo type: " + cargoType);
    }

    /**
     * Creates an exception for invalid customs status transitions (alternative signature).
     *
     * @param currentStatus the current customs status
     * @param targetStatus the attempted target status
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException invalidCustomsStatusTransition(String currentStatus, String targetStatus) {
        return new CargoOperationException(
            String.format("Invalid customs status transition from %s to %s", currentStatus, targetStatus));
    }

    /**
     * Creates an exception for dangerous cargo that is not properly cleared.
     *
     * @param cargoId the cargo ID (can be null)
     * @param reason the reason for the clearance issue
     * @return CargoOperationException with appropriate message
     */
    public static CargoOperationException dangerousCargoNotCleared(Long cargoId, String reason) {
        if (cargoId != null) {
            return new CargoOperationException(
                String.format("Dangerous cargo with ID %d not cleared: %s", cargoId, reason));
        } else {
            return new CargoOperationException("Dangerous cargo not cleared: " + reason);
        }
    }
}