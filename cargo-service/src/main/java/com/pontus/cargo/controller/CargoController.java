package com.pontus.cargo.controller;

import com.pontus.cargo.dto.CargoCreateRequest;
import com.pontus.cargo.dto.CargoResponse;
import com.pontus.cargo.dto.CargoUpdateRequest;
import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import com.pontus.cargo.service.CargoService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
@Slf4j
// @CrossOrigin(origins = "*") // Commented out - CORS handled by API Gateway
public class CargoController {
    
    private final CargoService cargoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER')")
    public ResponseEntity<CargoResponse> createCargo(@Valid @RequestBody CargoCreateRequest request) {
        log.info("Creating new cargo for vessel: {}", request.getVesselId());
        CargoResponse response = cargoService.createCargo(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<CargoResponse> getCargoById(@PathVariable Long id) {
        log.info("Retrieving cargo with ID: {}", id);
        CargoResponse response = cargoService.getCargoById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getAllCargo(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving all cargo");
        Page<CargoResponse> responses = cargoService.getAllCargo(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/vessel/{vesselId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getCargoByVessel(@PathVariable Long vesselId, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving cargo for vessel: {}", vesselId);
        Page<CargoResponse> responses = cargoService.getCargoByVessel(vesselId, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/customs-status/{status}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getCargoByCustomsStatus(
            @PathVariable CustomsStatus status,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving cargo with customs status: {}", status);
        Page<CargoResponse> responses = cargoService.getCargoByCustomsStatus(status, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/dangerous")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getDangerousCargo(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving dangerous cargo");
        Page<CargoResponse> responses = cargoService.getDangerousCargo(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/type/{cargoType}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getCargoByType(@PathVariable CargoType cargoType, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving cargo by type: {}", cargoType);
        Page<CargoResponse> responses = cargoService.getCargoByType(cargoType, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/origin/{origin}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getCargoByOrigin(@PathVariable String origin, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving cargo by origin: {}", origin);
        Page<CargoResponse> responses = cargoService.getCargoByOrigin(origin, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/destination/{destination}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CargoResponse>> getCargoByDestination(@PathVariable String destination, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving cargo by destination: {}", destination);
        Page<CargoResponse> responses = cargoService.getCargoByDestination(destination, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/weight-range")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<CargoResponse>> getCargoByWeightRange(@RequestParam BigDecimal minWeight, 
                                                                    @RequestParam BigDecimal maxWeight) {
        log.info("Retrieving cargo by weight range: {} - {}", minWeight, maxWeight);
        List<CargoResponse> responses = cargoService.getCargoByWeightRange(minWeight, maxWeight);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/dangerous/cleared")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<CargoResponse>> getDangerousCargoCleared() {
        log.info("Retrieving dangerous cargo that is cleared");
        List<CargoResponse> responses = cargoService.getDangerousCargoCleared();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/pending/ordered-by-date")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<CargoResponse>> getPendingCargoOrderedByDate() {
        log.info("Retrieving pending cargo ordered by date");
        List<CargoResponse> responses = cargoService.getPendingCargoOrderedByDate();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/vessel/{vesselId}/dangerous")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<CargoResponse>> getDangerousCargoByVessel(@PathVariable Long vesselId) {
        log.info("Retrieving dangerous cargo for vessel: {}", vesselId);
        List<CargoResponse> responses = cargoService.getDangerousCargoByVessel(vesselId);
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER')")
    public ResponseEntity<CargoResponse> updateCargo(@PathVariable Long id, 
                                                    @Valid @RequestBody CargoUpdateRequest request) {
        log.info("Updating cargo with ID: {}", id);
        CargoResponse response = cargoService.updateCargo(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/customs-status")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER')")
    public ResponseEntity<CargoResponse> updateCustomsStatus(@PathVariable Long id, 
                                                            @RequestParam CustomsStatus status) {
        log.info("Updating customs status for cargo {} to {}", id, status);
        CargoResponse response = cargoService.updateCustomsStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id) {
        log.info("Deleting cargo with ID: {}", id);
        cargoService.deleteCargo(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/vessel/{vesselId}/total-weight")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMS_OFFICER') or hasAuthority('DOCK_WORKER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<BigDecimal> getTotalWeightByVessel(@PathVariable Long vesselId) {
        log.info("Calculating total weight for vessel: {}", vesselId);
        BigDecimal totalWeight = cargoService.getTotalWeightByVessel(vesselId);
        return ResponseEntity.ok(totalWeight);
    }
}
