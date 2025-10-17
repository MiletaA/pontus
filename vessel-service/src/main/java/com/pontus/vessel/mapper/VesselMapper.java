package com.pontus.vessel.mapper;

import com.pontus.vessel.dto.VesselCreateRequest;
import com.pontus.vessel.dto.VesselResponse;
import com.pontus.vessel.dto.VesselUpdateRequest;
import com.pontus.vessel.entity.Vessel;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between Vessel entities and DTOs.
 * 
 * This class follows the Single Responsibility Principle by focusing solely on mapping operations.
 * It separates the concern of data transformation from business logic, making the code more maintainable.
 * 
 * Benefits:
 * - Centralized mapping logic
 * - Easy to test in isolation
 * - Follows DRY principle
 * - Can be easily extended with custom mapping logic
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
@Component
public class VesselMapper {
    
    /**
     * Converts a VesselCreateRequest DTO to a Vessel entity.
     * 
     * This method creates a new Vessel entity from the provided request data.
     * The status is automatically converted to uppercase for consistency.
     * 
     * @param request The vessel creation request containing vessel data
     * @return A new Vessel entity populated with data from the request
     * @throws IllegalArgumentException if request is null
     */
    public Vessel toEntity(VesselCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("VesselCreateRequest cannot be null");
        }
        
        Vessel vessel = new Vessel();
        vessel.setName(request.getName());
        vessel.setImoNumber(request.getImoNumber());
        vessel.setVesselType(request.getVesselType());
        vessel.setLength(request.getLength());
        vessel.setFlagCountry(request.getFlagCountry());
        vessel.setStatus(request.getStatus());
        vessel.setScheduledArrival(request.getScheduledArrival());
        vessel.setScheduledDeparture(request.getScheduledDeparture());
        vessel.setActualArrival(request.getActualArrival());
        vessel.setActualDeparture(request.getActualDeparture());
        
        return vessel;
    }
    
    /**
     * Converts a Vessel entity to a VesselResponse DTO.
     * 
     * This method creates a response DTO containing all vessel information
     * suitable for API responses. It includes all vessel fields including
     * the generated ID and timestamps.
     * 
     * @param vessel The vessel entity to convert
     * @return A VesselResponse DTO containing vessel data
     * @throws IllegalArgumentException if vessel is null
     */
    public VesselResponse toResponse(Vessel vessel) {
        if (vessel == null) {
            throw new IllegalArgumentException("Vessel cannot be null");
        }
        
        return new VesselResponse(
                vessel.getId(),
                vessel.getName(),
                vessel.getImoNumber(),
                vessel.getVesselType(),
                vessel.getLength(),
                vessel.getFlagCountry(),
                vessel.getStatus(),
                vessel.getScheduledArrival(),
                vessel.getScheduledDeparture(),
                vessel.getActualArrival(),
                vessel.getActualDeparture()
        );
    }
    
    /**
     * Updates an existing Vessel entity with data from a VesselUpdateRequest.
     * 
     * This method applies partial updates to a vessel entity. Only non-null fields
     * from the update request are applied to the entity. This allows for partial
     * updates without overwriting existing data with null values.
     * 
     * The method follows the principle of defensive programming by checking each
     * field individually before applying the update.
     * 
     * @param vessel The existing vessel entity to update
     * @param request The update request containing new data
     * @throws IllegalArgumentException if vessel or request is null
     */
    public void updateEntity(Vessel vessel, VesselUpdateRequest request) {
        if (vessel == null) {
            throw new IllegalArgumentException("Vessel cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("VesselUpdateRequest cannot be null");
        }
        
        // Update fields only if they are provided (not null)
        if (request.getName() != null) {
            vessel.setName(request.getName());
        }
        if (request.getImoNumber() != null) {
            vessel.setImoNumber(request.getImoNumber());
        }
        if (request.getVesselType() != null) {
            vessel.setVesselType(request.getVesselType());
        }
        if (request.getLength() != null) {
            vessel.setLength(request.getLength());
        }
        if (request.getFlagCountry() != null) {
            vessel.setFlagCountry(request.getFlagCountry());
        }
        if (request.getStatus() != null) {
            vessel.setStatus(request.getStatus());
        }
        if (request.getScheduledArrival() != null) {
            vessel.setScheduledArrival(request.getScheduledArrival());
        }
        if (request.getScheduledDeparture() != null) {
            vessel.setScheduledDeparture(request.getScheduledDeparture());
        }
        if (request.getActualArrival() != null) {
            vessel.setActualArrival(request.getActualArrival());
        }
        if (request.getActualDeparture() != null) {
            vessel.setActualDeparture(request.getActualDeparture());
        }
    }
}
