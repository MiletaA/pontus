package com.pontus.cargo.entity;

/**
 * Enumeration of valid cargo types following maritime industry standards.
 * 
 * This enum provides type safety and ensures only valid cargo types are used throughout the system.
 * The types cover the main categories of cargo handled in maritime ports.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public enum CargoType {
    
    /**
     * Containerized cargo transported in standard shipping containers.
     */
    CONTAINER("Containerized cargo transported in standard shipping containers"),
    
    /**
     * Liquid cargo transported in bulk (oil, chemicals, etc.).
     */
    BULK_LIQUID("Liquid cargo transported in bulk (oil, chemicals, etc.)"),
    
    /**
     * Dry cargo transported in bulk (grain, coal, ore, etc.).
     */
    BULK_DRY("Dry cargo transported in bulk (grain, coal, ore, etc.)"),
    
    /**
     * General cargo not containerized or in bulk.
     */
    GENERAL_CARGO("General cargo not containerized or in bulk"),
    
    /**
     * Cargo requiring refrigeration (food, pharmaceuticals, etc.).
     */
    REFRIGERATED("Cargo requiring refrigeration (food, pharmaceuticals, etc.)"),
    
    /**
     * Hazardous materials requiring special handling.
     */
    HAZARDOUS("Hazardous materials requiring special handling"),
    
    /**
     * Vehicles and automotive cargo.
     */
    AUTOMOTIVE("Vehicles and automotive cargo"),
    
    /**
     * Livestock and live animals.
     */
    LIVESTOCK("Livestock and live animals"),
    
    /**
     * Project cargo (oversized, heavy lift items).
     */
    PROJECT_CARGO("Project cargo (oversized, heavy lift items)"),
    
    /**
     * Other cargo types not covered by standard categories.
     */
    OTHER("Other cargo types not covered by standard categories");
    
    private final String description;
    
    CargoType(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the cargo type.
     * 
     * @return Description of what this cargo type represents
     */
    public String getDescription() {
        return description;
    }
}
