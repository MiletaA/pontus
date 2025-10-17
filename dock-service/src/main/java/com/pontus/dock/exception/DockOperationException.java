package com.pontus.dock.exception;

/**
 * Unified exception class for all dock-related business operations.
 * 
 * This exception replaces multiple specific exceptions (DockNotFoundException, etc.)
 * with a single, flexible exception that uses descriptive messages to indicate
 * the specific type of error that occurred.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public class DockOperationException extends RuntimeException {

    /**
     * Constructs a new DockOperationException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public DockOperationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DockOperationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the cause of the exception
     * @param cause the underlying cause of the exception
     */
    public DockOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Factory methods for common dock operation errors

    /**
     * Creates an exception for when a dock is not found.
     *
     * @param dockId the ID of the dock that was not found
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException dockNotFound(Long dockId) {
        return new DockOperationException(String.format("Dock with ID %d not found", dockId));
    }

    /**
     * Creates an exception for when a dock already exists.
     *
     * @param identifier the identifier of the dock that already exists
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException dockAlreadyExists(String identifier) {
        return new DockOperationException(String.format("Dock with identifier '%s' already exists", identifier));
    }

    /**
     * Creates an exception for invalid dock data.
     *
     * @param message the specific validation error message
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException invalidDockData(String message) {
        return new DockOperationException("Invalid dock data: " + message);
    }

    /**
     * Creates an exception for dock capacity issues.
     *
     * @param message the specific capacity error message
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException capacityError(String message) {
        return new DockOperationException("Dock capacity error: " + message);
    }

    /**
     * Creates an exception for dock assignment issues.
     *
     * @param message the specific assignment error message
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException assignmentError(String message) {
        return new DockOperationException("Dock assignment error: " + message);
    }

    /**
     * Creates an exception for invalid date ranges in dock operations.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @param reason the specific reason for the invalid date range
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException invalidDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String reason) {
        return new DockOperationException("Invalid date range from " + startDate + " to " + endDate + ": " + reason);
    }

    /**
     * Creates an exception for duplicate dock names.
     *
     * @param name the duplicate dock name
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException duplicateDockName(String name) {
        return new DockOperationException("Dock with name '" + name + "' already exists");
    }

    /**
     * Creates an exception for when a dock is already occupied.
     *
     * @param dockId the ID of the occupied dock
     * @param vesselId the ID of the vessel currently occupying the dock
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException dockAlreadyOccupied(Long dockId, Long vesselId) {
        return new DockOperationException(String.format("Dock %d is already occupied by vessel %d", dockId, vesselId));
    }

    /**
     * Creates an exception for when trying to release a vessel from an unoccupied dock.
     *
     * @param dockId the ID of the dock that is not occupied
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException dockNotOccupied(Long dockId) {
        return new DockOperationException(String.format("Dock %d is not currently occupied", dockId));
    }

    /**
     * Creates an exception for when trying to delete an occupied dock.
     *
     * @param dockId the ID of the occupied dock
     * @param vesselId the ID of the vessel occupying the dock
     * @return DockOperationException with appropriate message
     */
    public static DockOperationException cannotDeleteOccupiedDock(Long dockId, Long vesselId) {
        return new DockOperationException(String.format("Cannot delete dock %d: currently occupied by vessel %d", dockId, vesselId));
    }
}