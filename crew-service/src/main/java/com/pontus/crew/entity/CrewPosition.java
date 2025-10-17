package com.pontus.crew.entity;

/**
 * Enumeration of valid crew positions following maritime industry standards.
 * 
 * This enum provides type safety and ensures only valid crew positions are used throughout the system.
 * The positions cover the main roles aboard vessels in maritime operations.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public enum CrewPosition {
    
    /**
     * Master of the vessel, responsible for overall command and navigation.
     */
    CAPTAIN("Master of the vessel, responsible for overall command and navigation"),
    
    /**
     * Engineering personnel responsible for vessel machinery and systems.
     */
    ENGINEER("Engineering personnel responsible for vessel machinery and systems"),
    
    /**
     * Deck crew member responsible for general vessel operations.
     */
    SAILOR("Deck crew member responsible for general vessel operations"),
    
    /**
     * Galley staff responsible for food preparation and service.
     */
    COOK("Galley staff responsible for food preparation and service"),
    
    /**
     * Communications officer responsible for radio and electronic communications.
     */
    RADIO_OPERATOR("Communications officer responsible for radio and electronic communications"),
    
    /**
     * Officer responsible for deck operations and navigation duties.
     */
    DECK_OFFICER("Officer responsible for deck operations and navigation duties"),
    
    /**
     * Officer responsible for engine room operations and maintenance.
     */
    ENGINE_OFFICER("Officer responsible for engine room operations and maintenance");
    
    private final String description;
    
    CrewPosition(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the crew position.
     * 
     * @return Description of what this position represents
     */
    public String getDescription() {
        return description;
    }
}
