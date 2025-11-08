package com.pontus.dock.controller;

import com.pontus.dock.dto.DockCreateRequest;
import com.pontus.dock.dto.DockResponse;
import com.pontus.dock.dto.DockUpdateRequest;
import com.pontus.dock.service.DockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/docks")
@RequiredArgsConstructor
@Slf4j
// @CrossOrigin(origins = "*") // Commented out - CORS handled by API Gateway
public class DockController {
    
    private final DockService dockService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<DockResponse> createDock(@Valid @RequestBody DockCreateRequest request) {
        log.info("Creating new dock with name: {}", request.getName());
        DockResponse response = dockService.createDock(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<DockResponse> getDockById(@PathVariable Long id) {
        log.info("Retrieving dock with ID: {}", id);
        DockResponse response = dockService.getDockById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<DockResponse> getDockByName(@PathVariable String name) {
        log.info("Retrieving dock with name: {}", name);
        DockResponse response = dockService.getDockByName(name);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getAllDocks() {
        log.info("Retrieving all docks");
        List<DockResponse> responses = dockService.getAllDocks();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/occupied/{isOccupied}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getDocksByOccupiedStatus(@PathVariable Boolean isOccupied) {
        log.info("Retrieving docks with occupied status: {}", isOccupied);
        List<DockResponse> responses = dockService.getDocksByOccupiedStatus(isOccupied);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/dangerous-cargo/{handlesDangerous}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getDocksByDangerousCargoCapability(@PathVariable Boolean handlesDangerous) {
        log.info("Retrieving docks with dangerous cargo capability: {}", handlesDangerous);
        List<DockResponse> responses = dockService.getDocksByDangerousCargoCapability(handlesDangerous);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/available")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getAvailableDocks() {
        log.info("Retrieving all available docks");
        List<DockResponse> responses = dockService.getDocksByOccupiedStatus(false);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/available/vessel-length/{vesselLength}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getAvailableDocksForVessel(@PathVariable BigDecimal vesselLength) {
        log.info("Retrieving available docks for vessel length: {}", vesselLength);
        List<DockResponse> responses = dockService.getAvailableDocksForVessel(vesselLength);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/available/dangerous-cargo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getAvailableDangerousCargoCapableDocks() {
        log.info("Retrieving available dangerous cargo capable docks");
        List<DockResponse> responses = dockService.getAvailableDangerousCargoCapableDocks();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/vessel/{vesselId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<DockResponse>> getDocksByAssignedVessel(@PathVariable Long vesselId) {
        log.info("Retrieving docks assigned to vessel: {}", vesselId);
        List<DockResponse> responses = dockService.getDocksByAssignedVessel(vesselId);
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<DockResponse> updateDock(@PathVariable Long id, 
                                                  @Valid @RequestBody DockUpdateRequest request) {
        log.info("Updating dock with ID: {}", id);
        DockResponse response = dockService.updateDock(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteDock(@PathVariable Long id) {
        log.info("Deleting dock with ID: {}", id);
        dockService.deleteDock(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{dockId}/assign-vessel/{vesselId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<DockResponse> assignVesselToDock(@PathVariable Long dockId, 
                                                          @PathVariable Long vesselId,
                                                          @RequestParam LocalDateTime scheduledFrom,
                                                          @RequestParam LocalDateTime scheduledTo) {
        log.info("Assigning vessel {} to dock {}", vesselId, dockId);
        DockResponse response = dockService.assignVesselToDock(dockId, vesselId, scheduledFrom, scheduledTo);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{dockId}/release-vessel")
    public ResponseEntity<DockResponse> releaseVesselFromDock(@PathVariable Long dockId) {
        log.info("Releasing vessel from dock {}", dockId);
        DockResponse response = dockService.releaseVesselFromDock(dockId);
        return ResponseEntity.ok(response);
    }
}
