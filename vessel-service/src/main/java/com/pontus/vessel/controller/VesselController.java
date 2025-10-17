package com.pontus.vessel.controller;

import com.pontus.vessel.dto.VesselCreateRequest;
import com.pontus.vessel.dto.VesselResponse;
import com.pontus.vessel.dto.VesselUpdateRequest;
import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.enums.VesselType;
import com.pontus.vessel.service.VesselService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vessels")
@RequiredArgsConstructor
@Slf4j
// @CrossOrigin(origins = "*") // Commented out - CORS handled by API Gateway
public class VesselController {
    
    private final VesselService vesselService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<VesselResponse> createVessel(@Valid @RequestBody VesselCreateRequest request) {
        log.info("Creating new vessel with name: {}", request.getName());
        VesselResponse response = vesselService.createVessel(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<VesselResponse> getVesselById(@PathVariable Long id) {
        log.info("Retrieving vessel with ID: {}", id);
        VesselResponse response = vesselService.getVesselById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/imo/{imoNumber}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<VesselResponse> getVesselByImoNumber(@PathVariable String imoNumber) {
        log.info("Retrieving vessel with IMO number: {}", imoNumber);
        VesselResponse response = vesselService.getVesselByImoNumber(imoNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<Page<VesselResponse>> getAllVessels(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving all vessels");
        Page<VesselResponse> responses = vesselService.getAllVessels(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<Page<VesselResponse>> getVesselsByStatus(@PathVariable VesselStatus status, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving vessels with status: {}", status);
        Page<VesselResponse> responses = vesselService.getVesselsByStatus(status, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/type/{vesselType}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<Page<VesselResponse>> getVesselsByType(@PathVariable VesselType vesselType, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving vessels with type: {}", vesselType);
        Page<VesselResponse> responses = vesselService.getVesselsByType(vesselType, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/expected-arrivals")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<List<VesselResponse>> getVesselsExpectedToArrive() {
        log.info("Retrieving vessels expected to arrive");
        List<VesselResponse> responses = vesselService.getVesselsExpectedToArrive();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/expected-departures")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<List<VesselResponse>> getVesselsExpectedToDepart() {
        log.info("Retrieving vessels expected to depart");
        List<VesselResponse> responses = vesselService.getVesselsExpectedToDepart();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/in-port")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<List<VesselResponse>> getVesselsCurrentlyInPort() {
        log.info("Retrieving vessels currently in port");
        List<VesselResponse> responses = vesselService.getVesselsCurrentlyInPort();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<VesselResponse> updateVessel(@PathVariable Long id, 
                                                      @Valid @RequestBody VesselUpdateRequest request) {
        log.info("Updating vessel with ID: {}", id);
        VesselResponse response = vesselService.updateVessel(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteVessel(@PathVariable Long id) {
        log.info("Deleting vessel with ID: {}", id);
        vesselService.deleteVessel(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/arrival")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<VesselResponse> updateVesselArrival(@PathVariable Long id, 
                                                             @RequestParam LocalDateTime actualArrival) {
        log.info("Updating arrival for vessel with ID: {}", id);
        VesselResponse response = vesselService.updateVesselArrival(id, actualArrival);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/departure")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<VesselResponse> updateVesselDeparture(@PathVariable Long id, 
                                                               @RequestParam LocalDateTime actualDeparture) {
        log.info("Updating departure for vessel with ID: {}", id);
        VesselResponse response = vesselService.updateVesselDeparture(id, actualDeparture);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS') or hasAuthority('USER')")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Vessel Service is running");
    }
}
