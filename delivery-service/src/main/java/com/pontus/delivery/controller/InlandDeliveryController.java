package com.pontus.delivery.controller;

import com.pontus.delivery.dto.InlandDeliveryCreateRequest;
import com.pontus.delivery.dto.InlandDeliveryResponse;
import com.pontus.delivery.dto.InlandDeliveryUpdateRequest;
import com.pontus.delivery.entity.DeliveryStatus;
import com.pontus.delivery.service.InlandDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Slf4j
// @CrossOrigin(origins = "*") // Commented out - CORS handled by API Gateway
public class InlandDeliveryController {
    
    private final InlandDeliveryService deliveryService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<InlandDeliveryResponse> createDelivery(@Valid @RequestBody InlandDeliveryCreateRequest request) {
        log.info("Creating new inland delivery for cargo: {}", request.getCargoId());
        InlandDeliveryResponse response = deliveryService.createDelivery(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<InlandDeliveryResponse> getDeliveryById(@PathVariable Long id) {
        log.info("Retrieving inland delivery with ID: {}", id);
        InlandDeliveryResponse response = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getAllDeliveries() {
        log.info("Retrieving all inland deliveries");
        List<InlandDeliveryResponse> responses = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/cargo/{cargoId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getDeliveriesByCargoId(@PathVariable Long cargoId) {
        log.info("Retrieving inland deliveries for cargo: {}", cargoId);
        List<InlandDeliveryResponse> responses = deliveryService.getDeliveriesByCargoId(cargoId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getDeliveriesByStatus(@PathVariable DeliveryStatus status) {
        log.info("Retrieving inland deliveries by status: {}", status);
        List<InlandDeliveryResponse> responses = deliveryService.getDeliveriesByStatus(status);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/vehicle/{vehicleRegistration}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getDeliveriesByVehicle(@PathVariable String vehicleRegistration) {
        log.info("Retrieving inland deliveries for vehicle: {}", vehicleRegistration);
        List<InlandDeliveryResponse> responses = deliveryService.getDeliveriesByVehicle(vehicleRegistration);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/driver/{driverName}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getDeliveriesByDriver(@PathVariable String driverName) {
        log.info("Retrieving inland deliveries for driver: {}", driverName);
        List<InlandDeliveryResponse> responses = deliveryService.getDeliveriesByDriver(driverName);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/destination")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getDeliveriesByDestination(@RequestParam String address) {
        log.info("Retrieving inland deliveries by destination: {}", address);
        List<InlandDeliveryResponse> responses = deliveryService.getDeliveriesByDestination(address);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getOverdueDeliveries() {
        log.info("Retrieving overdue deliveries");
        List<InlandDeliveryResponse> responses = deliveryService.getOverdueDeliveries();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/upcoming")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getUpcomingDeliveries(@RequestParam(defaultValue = "24") int hours) {
        log.info("Retrieving upcoming deliveries within {} hours", hours);
        List<InlandDeliveryResponse> responses = deliveryService.getUpcomingDeliveries(hours);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getCompletedDeliveries() {
        log.info("Retrieving completed deliveries");
        List<InlandDeliveryResponse> responses = deliveryService.getCompletedDeliveries();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getFailedDeliveries() {
        log.info("Retrieving failed deliveries");
        List<InlandDeliveryResponse> responses = deliveryService.getFailedDeliveries();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<InlandDeliveryResponse>> getActiveDeliveries() {
        log.info("Retrieving active deliveries");
        List<InlandDeliveryResponse> responses = deliveryService.getActiveDeliveries();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/drivers")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<String>> getDistinctDriverNames() {
        log.info("Retrieving distinct driver names");
        List<String> drivers = deliveryService.getDistinctDriverNames();
        return ResponseEntity.ok(drivers);
    }
    
    @GetMapping("/vehicles")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER') or hasAuthority('OPERATIONS')")
    public ResponseEntity<List<String>> getDistinctVehicleRegistrations() {
        log.info("Retrieving distinct vehicle registrations");
        List<String> vehicles = deliveryService.getDistinctVehicleRegistrations();
        return ResponseEntity.ok(vehicles);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<InlandDeliveryResponse> updateDelivery(@PathVariable Long id, 
                                                               @Valid @RequestBody InlandDeliveryUpdateRequest request) {
        log.info("Updating inland delivery with ID: {}", id);
        InlandDeliveryResponse response = deliveryService.updateDelivery(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<InlandDeliveryResponse> updateDeliveryStatus(@PathVariable Long id, 
                                                                      @RequestParam DeliveryStatus status) {
        log.info("Updating delivery status for ID {} to {}", id, status);
        InlandDeliveryResponse response = deliveryService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/delivered")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('HARBOR_MASTER')")
    public ResponseEntity<InlandDeliveryResponse> markAsDelivered(@PathVariable Long id) {
        log.info("Marking delivery {} as delivered", id);
        InlandDeliveryResponse response = deliveryService.markAsDelivered(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        log.info("Deleting inland delivery with ID: {}", id);
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
    
}
