package com.pontus.dock.mapper;

import com.pontus.dock.dto.DockCreateRequest;
import com.pontus.dock.dto.DockResponse;
import com.pontus.dock.dto.DockUpdateRequest;
import com.pontus.dock.entity.Dock;
import org.springframework.stereotype.Component;

@Component
public class DockMapper {
    
    public Dock toEntity(DockCreateRequest request) {
        Dock dock = new Dock();
        dock.setName(request.getName());
        dock.setMaxLength(request.getMaxLength());
        dock.setHandlesDangerous(request.getHandlesDangerous());
        dock.setDescription(request.getDescription());
        dock.setIsOccupied(false);
        return dock;
    }
    
    public DockResponse toResponse(Dock dock) {
        DockResponse response = new DockResponse();
        response.setId(dock.getId());
        response.setName(dock.getName());
        response.setMaxLength(dock.getMaxLength());
        response.setIsOccupied(dock.getIsOccupied());
        response.setAssignedVesselId(dock.getAssignedVesselId());
        response.setScheduledFrom(dock.getScheduledFrom());
        response.setScheduledTo(dock.getScheduledTo());
        response.setHandlesDangerous(dock.getHandlesDangerous());
        response.setDescription(dock.getDescription());
        response.setCreatedAt(dock.getCreatedAt());
        response.setUpdatedAt(dock.getUpdatedAt());
        return response;
    }
    
    public void updateEntity(Dock dock, DockUpdateRequest request) {
        dock.setName(request.getName());
        dock.setMaxLength(request.getMaxLength());
        dock.setHandlesDangerous(request.getHandlesDangerous());
        dock.setDescription(request.getDescription());
    }
}
