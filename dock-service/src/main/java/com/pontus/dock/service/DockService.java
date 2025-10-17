package com.pontus.dock.service;

import com.pontus.dock.dto.*;
import com.pontus.dock.entity.Dock;
import com.pontus.dock.exception.DockOperationException;
import com.pontus.dock.mapper.DockMapper;
import com.pontus.dock.repository.DockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DockService {

    private final DockRepository dockRepository;
    private final DockValidationService validationService;
    private final DockMapper dockMapper;

    public DockResponse createDock(DockCreateRequest request) {
        log.info("Creating new dock with name: {}", request.getName());
        
        // Validate dock creation data
        validationService.validateDockCreation(
            request.getName(),
            request.getMaxLength(),
            request.getHandlesDangerous(),
            request.getDescription()
        );
        
        // Check for duplicate dock names
        if (dockRepository.existsByName(request.getName())) {
            throw DockOperationException.duplicateDockName(request.getName());
        }
        
        Dock dock = dockMapper.toEntity(request);
        dock.setCreatedAt(LocalDateTime.now());
        dock.setUpdatedAt(LocalDateTime.now());
        
        Dock savedDock = dockRepository.save(dock);
        log.info("Successfully created dock with ID: {}", savedDock.getId());
        return dockMapper.toResponse(savedDock);
    }

    @Transactional(readOnly = true)
    public DockResponse getDockById(Long id) {
        log.info("Retrieving dock with ID: {}", id);
        Dock dock = dockRepository.findById(id)
            .orElseThrow(() -> DockOperationException.dockNotFound(id));
        return dockMapper.toResponse(dock);
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getAllDocks() {
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getAvailableDocks() {
        log.info("Retrieving available docks");
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getOccupiedDocks() {
        log.info("Retrieving occupied docks");
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksByVessel(Long vesselId) {
        log.info("Retrieving docks for vessel: {}", vesselId);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksByMinLength(Double minLength) {
        log.info("Retrieving docks with minimum length: {}", minLength);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDangerousCargoCapableDocks() {
        log.info("Retrieving docks capable of handling dangerous cargo");
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksWithScheduledArrivals() {
        log.info("Retrieving docks with scheduled arrivals");
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public DockResponse getDockByName(String name) {
        log.info("Retrieving dock with name: {}", name);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Dock not found: " + name));
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksByOccupiedStatus(Boolean isOccupied) {
        log.info("Retrieving docks with occupied status: {}", isOccupied);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksByDangerousCargoCapability(Boolean handlesDangerous) {
        log.info("Retrieving docks with dangerous cargo capability: {}", handlesDangerous);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getAvailableDocksForVessel(java.math.BigDecimal vesselLength) {
        log.info("Retrieving available docks for vessel length: {}", vesselLength);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getAvailableDangerousCargoCapableDocks() {
        log.info("Retrieving available dangerous cargo capable docks");
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DockResponse> getDocksByAssignedVessel(Long vesselId) {
        log.info("Retrieving docks assigned to vessel: {}", vesselId);
        
        return dockRepository.findAll().stream()
            .map(dockMapper::toResponse)
            .toList();
    }

    public DockResponse updateDock(Long id, DockUpdateRequest request) {
        log.info("Updating dock with ID: {}", id);
        
        Dock existingDock = dockRepository.findById(id)
            .orElseThrow(() -> DockOperationException.dockNotFound(id));
        
        // Validate dock update data
        validationService.validateDockUpdate(
            request.getName(),
            request.getMaxLength(),
            request.getHandlesDangerous(),
            request.getDescription(),
            existingDock.getIsOccupied(),
            existingDock.getAssignedVesselId(),
            existingDock.getScheduledFrom(),
            existingDock.getScheduledTo()
        );
        
        // Check for duplicate dock names (excluding current dock)
        if (!existingDock.getName().equals(request.getName()) && 
            dockRepository.existsByName(request.getName())) {
            throw DockOperationException.duplicateDockName(request.getName());
        }
        
        dockMapper.updateEntity(existingDock, request);
        existingDock.setUpdatedAt(LocalDateTime.now());
        
        Dock updatedDock = dockRepository.save(existingDock);
        log.info("Successfully updated dock with ID: {}", id);
        
        return dockMapper.toResponse(updatedDock);
    }

    public DockResponse assignVesselToDock(Long dockId, Long vesselId) {
        log.info("Assigning vessel {} to dock {}", vesselId, dockId);
        
        Dock dock = dockRepository.findById(dockId)
            .orElseThrow(() -> DockOperationException.dockNotFound(dockId));
        
        // Validate vessel assignment
        if (dock.getIsOccupied()) {
            throw DockOperationException.dockAlreadyOccupied(dockId, dock.getAssignedVesselId());
        }
        
        validationService.validateOccupationStatus(true, vesselId, null, null);
        
        dock.setIsOccupied(true);
        dock.setAssignedVesselId(vesselId);
        dock.setUpdatedAt(LocalDateTime.now());
        
        Dock updatedDock = dockRepository.save(dock);
        log.info("Successfully assigned vessel {} to dock {}", vesselId, dockId);
        return dockMapper.toResponse(updatedDock);
    }

    public DockResponse assignVesselToDock(Long dockId, Long vesselId, LocalDateTime scheduledFrom, LocalDateTime scheduledTo) {
        log.info("Assigning vessel {} to dock {} from {} to {}", vesselId, dockId, scheduledFrom, scheduledTo);
        
        Dock dock = dockRepository.findById(dockId)
            .orElseThrow(() -> DockOperationException.dockNotFound(dockId));
        
        // Validate vessel assignment with scheduling
        if (dock.getIsOccupied()) {
            throw DockOperationException.dockAlreadyOccupied(dockId, dock.getAssignedVesselId());
        }
        
        validationService.validateScheduledDates(scheduledFrom, scheduledTo);
        validationService.validateOccupationStatus(true, vesselId, scheduledFrom, scheduledTo);
        
        dock.setIsOccupied(true);
        dock.setAssignedVesselId(vesselId);
        dock.setScheduledFrom(scheduledFrom);
        dock.setScheduledTo(scheduledTo);
        dock.setUpdatedAt(LocalDateTime.now());
        
        Dock updatedDock = dockRepository.save(dock);
        log.info("Successfully assigned vessel {} to dock {} with schedule", vesselId, dockId);
        return dockMapper.toResponse(updatedDock);
    }

    public DockResponse releaseVesselFromDock(Long dockId) {
        log.info("Releasing vessel from dock {}", dockId);
        
        Dock dock = dockRepository.findById(dockId)
            .orElseThrow(() -> DockOperationException.dockNotFound(dockId));
        
        // Validate that dock is actually occupied
        if (!dock.getIsOccupied()) {
            throw DockOperationException.dockNotOccupied(dockId);
        }
        
        dock.setIsOccupied(false);
        dock.setAssignedVesselId(null);
        dock.setScheduledFrom(null);
        dock.setScheduledTo(null);
        dock.setUpdatedAt(LocalDateTime.now());
        
        Dock updatedDock = dockRepository.save(dock);
        log.info("Successfully released vessel from dock {}", dockId);
        return dockMapper.toResponse(updatedDock);
    }

    public void deleteDock(Long id) {
        log.info("Deleting dock with ID: {}", id);
        
        Dock dock = dockRepository.findById(id)
            .orElseThrow(() -> DockOperationException.dockNotFound(id));
        
        // Validate that dock is not occupied before deletion
        if (dock.getIsOccupied()) {
            throw DockOperationException.cannotDeleteOccupiedDock(id, dock.getAssignedVesselId());
        }
        
        dockRepository.deleteById(id);
        log.info("Successfully deleted dock with ID: {}", id);
    }

}