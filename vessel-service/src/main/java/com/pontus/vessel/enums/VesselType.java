package com.pontus.vessel.enums;

/**
 * Enumeration of valid vessel types following maritime industry standards.
 * 
 * This enum provides type safety and ensures only valid vessel types are used throughout the system.
 * The types cover the main categories of vessels that visit maritime ports.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public enum VesselType {
    
    /**
     * Vessels designed to carry containerized cargo.
     */
    CONTAINER_SHIP("Vessels designed to carry containerized cargo"),
    
    /**
     * Vessels designed to carry dry bulk cargo (grain, coal, ore).
     */
    BULK_CARRIER("Vessels designed to carry dry bulk cargo (grain, coal, ore)"),
    
    /**
     * Vessels designed to carry liquid cargo (oil, chemicals).
     */
    TANKER("Vessels designed to carry liquid cargo (oil, chemicals)"),
    
    /**
     * General cargo vessels for non-containerized freight.
     */
    CARGO_SHIP("General cargo vessels for non-containerized freight"),
    
    /**
     * Passenger vessels for tourism and leisure.
     */
    CRUISE_SHIP("Passenger vessels for tourism and leisure"),
    
    /**
     * Vessels for passenger and vehicle transport on regular routes.
     */
    FERRY("Vessels for passenger and vehicle transport on regular routes"),
    
    /**
     * Vessels designed to carry vehicles and automotive cargo.
     */
    RO_RO("Vessels designed to carry vehicles and automotive cargo (Roll-on/Roll-off)"),
    
    /**
     * Vessels designed to carry refrigerated cargo.
     */
    REEFER("Vessels designed to carry refrigerated cargo"),
    
    /**
     * Vessels for transporting liquefied natural gas.
     */
    LNG_CARRIER("Vessels for transporting liquefied natural gas"),
    
    /**
     * Vessels for transporting liquefied petroleum gas.
     */
    LPG_CARRIER("Vessels for transporting liquefied petroleum gas"),
    
    /**
     * Specialized vessels for heavy lift and project cargo.
     */
    HEAVY_LIFT("Specialized vessels for heavy lift and project cargo"),
    
    /**
     * Fishing vessels for commercial fishing operations.
     */
    FISHING_VESSEL("Fishing vessels for commercial fishing operations"),
    
    /**
     * Tugboats for assisting other vessels.
     */
    TUG("Tugboats for assisting other vessels"),
    
    /**
     * Supply vessels for offshore operations.
     */
    SUPPLY_VESSEL("Supply vessels for offshore operations"),
    
    /**
     * Other vessel types not covered by standard categories.
     */
    OTHER("Other vessel types not covered by standard categories");
    
    private final String description;
    
    VesselType(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the vessel type.
     * 
     * @return Description of what this vessel type represents
     */
    public String getDescription() {
        return description;
    }
}
