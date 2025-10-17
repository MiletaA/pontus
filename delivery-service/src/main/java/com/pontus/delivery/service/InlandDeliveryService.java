package com.pontus.delivery.service;

import com.pontus.delivery.dto.InlandDeliveryCreateRequest;
import com.pontus.delivery.dto.InlandDeliveryResponse;
import com.pontus.delivery.dto.InlandDeliveryUpdateRequest;
import com.pontus.delivery.entity.DeliveryStatus;
import com.pontus.delivery.entity.InlandDelivery;
import com.pontus.delivery.exception.DeliveryOperationException;
import com.pontus.delivery.mapper.InlandDeliveryMapper;
import com.pontus.delivery.repository.InlandDeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InlandDeliveryService {
    
    private final InlandDeliveryRepository deliveryRepository;
    private final DeliveryValidationService validationService;
    private final InlandDeliveryMapper deliveryMapper;
    
    public InlandDeliveryResponse createDelivery(InlandDeliveryCreateRequest request) {
        log.info("Creating new delivery for cargo ID: {}", request.getCargoId());
        
        validationService.validateScheduledDeliveryTime(request.getScheduledDeliveryTime());
        validationService.validateVehicleRegistration(request.getVehicleRegistration());
        validationService.validateDriverName(request.getDriverName());
        validationService.validateDestinationAddress(request.getDestinationAddress());
        validationService.validateNotes(request.getNotes());
        
        InlandDelivery delivery = deliveryMapper.toEntity(request);
        InlandDelivery savedDelivery = deliveryRepository.save(delivery);
        
        return deliveryMapper.toResponse(savedDelivery);
    }
    
    @Transactional(readOnly = true)
    public InlandDeliveryResponse getDeliveryById(Long id) {
        log.info("Retrieving inland delivery with ID: {}", id);
        InlandDelivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> DeliveryOperationException.deliveryNotFound(id));
        return deliveryMapper.toResponse(delivery);
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getAllDeliveries() {
        log.info("Retrieving all inland deliveries");
        return deliveryRepository.findAll().stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getDeliveriesByCargoId(Long cargoId) {
        log.info("Retrieving inland deliveries for cargo: {}", cargoId);
        return deliveryRepository.findByCargoId(cargoId).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getDeliveriesByStatus(DeliveryStatus status) {
        log.info("Retrieving inland deliveries by status: {}", status);
        return deliveryRepository.findByDeliveryStatus(status).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getDeliveriesByVehicle(String vehicleRegistration) {
        log.info("Retrieving inland deliveries for vehicle: {}", vehicleRegistration);
        return deliveryRepository.findByVehicleRegistration(vehicleRegistration).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getDeliveriesByDriver(String driverName) {
        log.info("Retrieving inland deliveries for driver: {}", driverName);
        return deliveryRepository.findByDriverName(driverName).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getDeliveriesByDestination(String address) {
        log.info("Retrieving inland deliveries by destination: {}", address);
        return deliveryRepository.findByDestinationAddressContaining(address).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getOverdueDeliveries() {
        log.info("Retrieving overdue deliveries");
        return deliveryRepository.findOverdueDeliveries(LocalDateTime.now()).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getUpcomingDeliveries(int hours) {
        log.info("Retrieving upcoming deliveries within {} hours", hours);
        LocalDateTime endTime = LocalDateTime.now().plusHours(hours);
        return deliveryRepository.findUpcomingDeliveries(LocalDateTime.now(), endTime).stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getCompletedDeliveries() {
        log.info("Retrieving completed deliveries");
        return deliveryRepository.findCompletedDeliveries().stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getFailedDeliveries() {
        log.info("Retrieving failed deliveries");
        return deliveryRepository.findFailedDeliveries().stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InlandDeliveryResponse> getActiveDeliveries() {
        log.info("Retrieving active deliveries");
        return deliveryRepository.findActiveDeliveries().stream()
            .map(deliveryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctDriverNames() {
        log.info("Retrieving distinct driver names");
        return deliveryRepository.findDistinctDriverNames();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctVehicleRegistrations() {
        log.info("Retrieving distinct vehicle registrations");
        return deliveryRepository.findDistinctVehicleRegistrations();
    }
    
    public InlandDeliveryResponse updateDelivery(Long id, InlandDeliveryUpdateRequest request) {
        InlandDelivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> DeliveryOperationException.deliveryNotFound(id));
        
        if (request.getScheduledDeliveryTime() != null) {
            validationService.validateScheduledDeliveryTime(request.getScheduledDeliveryTime());
        }
        if (request.getVehicleRegistration() != null) {
            validationService.validateVehicleRegistration(request.getVehicleRegistration());
        }
        if (request.getDriverName() != null) {
            validationService.validateDriverName(request.getDriverName());
        }
        if (request.getDestinationAddress() != null) {
            validationService.validateDestinationAddress(request.getDestinationAddress());
        }
        if (request.getNotes() != null) {
            validationService.validateNotes(request.getNotes());
        }
        
        deliveryMapper.updateEntity(delivery, request);
        InlandDelivery updatedDelivery = deliveryRepository.save(delivery);
        
        return deliveryMapper.toResponse(updatedDelivery);
    }
    
    public InlandDeliveryResponse updateDeliveryStatus(Long id, DeliveryStatus newStatus) {
        InlandDelivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> DeliveryOperationException.deliveryNotFound(id));
        
        DeliveryStatus currentStatus = delivery.getDeliveryStatus();
        validationService.validateStatusTransition(currentStatus, newStatus);
        
        delivery.setDeliveryStatus(newStatus);
        
        if (newStatus == DeliveryStatus.DELIVERED) {
            LocalDateTime actualTime = LocalDateTime.now();
            validationService.validateActualDeliveryTime(actualTime, delivery.getScheduledDeliveryTime());
            delivery.setDeliveryTime(actualTime);
        }
        
        InlandDelivery updatedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toResponse(updatedDelivery);
    }
    
    public InlandDeliveryResponse markAsDelivered(Long id) {
        log.info("Marking delivery {} as delivered", id);
        
        InlandDelivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> DeliveryOperationException.deliveryNotFound(id));
        
        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw DeliveryOperationException.deliveryAlreadyCompleted(id);
        }
        
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveryTime(LocalDateTime.now());
        
        InlandDelivery updatedDelivery = deliveryRepository.save(delivery);
        
        log.info("Successfully marked delivery {} as delivered", id);
        return deliveryMapper.toResponse(updatedDelivery);
    }
    
    public void deleteDelivery(Long id) {
        log.info("Deleting inland delivery with ID: {}", id);
        
        InlandDelivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> DeliveryOperationException.deliveryNotFound(id));
        
        // Prevent deletion of completed deliveries
        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw DeliveryOperationException.deliveryAlreadyCompleted(id);
        }
        
        deliveryRepository.delete(delivery);
        log.info("Successfully deleted inland delivery with ID: {}", id);
    }


    

}
