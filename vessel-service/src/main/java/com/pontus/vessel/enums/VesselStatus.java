 package com.pontus.vessel.enums;

/**
 * Enumeration of valid vessel statuses following maritime industry standards.
 * 
 * This enum provides type safety and ensures only valid statuses are used throughout the system.
 * The statuses follow the natural lifecycle of a vessel visit to a port.
 * 
 * Status Flow:
 * SCHEDULED → UNDERWAY → ANCHORED → BERTHED → DEPARTED
 * 
 * Alternative flows:
 * - DELAYED can occur at any stage
 * - CANCELLED can occur before DEPARTED
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public enum VesselStatus {
    
    /**
     * Vessel visit is planned and scheduled.
     * Initial status when a vessel visit is registered in the system.
     */
    SCHEDULED("Vessel visit is planned and scheduled"),
    
    /**
     * Vessel is en route to the port.
     * Status when vessel has departed from previous port and is traveling to this port.
     */
    UNDERWAY("Vessel is en route to the port"),
    
    /**
     * Vessel is waiting in anchorage area.
     * Status when vessel has arrived at port waters but is waiting for berth assignment.
     */
    ANCHORED("Vessel is waiting in anchorage area"),
    
    /**
     * Vessel is docked at a specific berth/dock.
     * Status when vessel is physically moored and conducting port operations.
     */
    BERTHED("Vessel is docked at a specific berth/dock"),
    
    /**
     * Vessel has left the port.
     * Final status when vessel has completed port operations and departed.
     */
    DEPARTED("Vessel has left the port"),
    
    /**
     * Vessel schedule is delayed.
     * Can apply to any stage - arrival delay, berthing delay, or departure delay.
     */
    DELAYED("Vessel schedule is delayed (can apply to any stage)"),
    
    /**
     * Vessel visit has been cancelled.
     * Status when a scheduled vessel visit is cancelled before completion.
     */
    CANCELLED("Vessel visit has been cancelled");
    
    private final String description;
    
    VesselStatus(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the status.
     * 
     * @return Description of what this status means
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this status indicates the vessel is physically present at the port.
     * 
     * @return true if vessel is at the port (ANCHORED or BERTHED)
     */
    public boolean isAtPort() {
        return this == ANCHORED || this == BERTHED;
    }
    
    /**
     * Checks if this status indicates the vessel visit is still active.
     * 
     * @return true if vessel visit is not completed (not DEPARTED or CANCELLED)
     */
    public boolean isActive() {
        return this != DEPARTED && this != CANCELLED;
    }
    
    /**
     * Checks if this status indicates the vessel can be assigned to a dock.
     * 
     * @return true if vessel is ready for dock assignment (ANCHORED)
     */
    public boolean canBeAssignedToDock() {
        return this == ANCHORED;
    }
    
    /**
     * Gets the next logical status in the vessel lifecycle.
     * 
     * @return The next expected status, or null if this is a terminal status
     */
    public VesselStatus getNextStatus() {
        return switch (this) {
            case SCHEDULED -> UNDERWAY;
            case UNDERWAY -> ANCHORED;
            case ANCHORED -> BERTHED;
            case BERTHED -> DEPARTED;
            case DEPARTED, CANCELLED, DELAYED -> null; // Terminal or special statuses
        };
    }
    
    /**
     * Checks if transition to another status is valid.
     * 
     * @param newStatus The status to transition to
     * @return true if the transition is valid
     */
    public boolean canTransitionTo(VesselStatus newStatus) {
        // DELAYED and CANCELLED can be set from any status except DEPARTED
        if (newStatus == DELAYED || newStatus == CANCELLED) {
            return this != DEPARTED;
        }
        
        // Normal flow transitions
        return switch (this) {
            case SCHEDULED -> newStatus == UNDERWAY;
            case UNDERWAY -> newStatus == ANCHORED;
            case ANCHORED -> newStatus == BERTHED;
            case BERTHED -> newStatus == DEPARTED;
            case DEPARTED -> false; // Terminal status
            case DELAYED -> newStatus == getNextStatus() || newStatus == CANCELLED;
            case CANCELLED -> false; // Terminal status
        };
    }
}
