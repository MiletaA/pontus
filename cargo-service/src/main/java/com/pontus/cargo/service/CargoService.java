package com.pontus.cargo.service;

import com.pontus.cargo.dto.*;
import com.pontus.cargo.entity.Cargo;
import com.pontus.cargo.entity.CargoType;
import com.pontus.cargo.entity.CustomsStatus;
import com.pontus.cargo.exception.CargoOperationException;
import com.pontus.cargo.mapper.CargoMapper;
import com.pontus.cargo.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CargoService {

    private final CargoRepository cargoRepository;
    private final CargoMapper cargoMapper;
    private final CargoValidationService validationService;

    public CargoResponse createCargo(CargoCreateRequest request) {
        log.info("Creating new cargo with description: {}", request.getDescription());
        
        // Validate cargo data
        validationService.validateCargoData(request);
        
        Cargo cargo = cargoMapper.toEntity(request);
        cargo.setCustomsStatus(CustomsStatus.PENDING);
        cargo.setCreatedAt(LocalDateTime.now());
        cargo.setUpdatedAt(LocalDateTime.now());
        
        Cargo savedCargo = cargoRepository.save(cargo);
        log.info("Successfully created cargo with ID: {}", savedCargo.getId());
        
        return cargoMapper.toResponse(savedCargo);
    }


    @Transactional(readOnly = true)
    public CargoResponse getCargoById(Long cargoId) {
        log.info("Retrieving cargo with ID: {}", cargoId);
        
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> CargoOperationException.cargoNotFound(cargoId));
        
        return cargoMapper.toResponse(cargo);
    }


    @Transactional(readOnly = true)
    public Page<CargoResponse> getAllCargo(Pageable pageable) {
        log.info("Retrieving all cargo with pagination: {}", pageable);
        
        Page<Cargo> cargoPage = cargoRepository.findAll(pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    



    public CargoResponse updateCargo(Long cargoId, CargoUpdateRequest request) {
        log.info("Updating cargo with ID: {}", cargoId);
        
        Cargo existingCargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> CargoOperationException.cargoNotFound(cargoId));
        
        // Validate update data
        validationService.validateCargoUpdateData(request);
        
        // Update fields
        cargoMapper.updateEntity(existingCargo, request);
        existingCargo.setUpdatedAt(LocalDateTime.now());
        
        Cargo updatedCargo = cargoRepository.save(existingCargo);
        log.info("Successfully updated cargo with ID: {}", cargoId);
        
        return cargoMapper.toResponse(updatedCargo);
    }


    public void deleteCargo(Long cargoId) {
        log.info("Deleting cargo with ID: {}", cargoId);
        
        if (!cargoRepository.existsById(cargoId)) {
            throw CargoOperationException.cargoNotFound(cargoId);
        }
        
        cargoRepository.deleteById(cargoId);
        log.info("Successfully deleted cargo with ID: {}", cargoId);
    }


    public CargoResponse updateCustomsStatus(Long cargoId, CustomsStatus newStatus) {
        log.info("Updating customs status for cargo ID: {} to {}", cargoId, newStatus);
        
        Cargo cargo = cargoRepository.findById(cargoId)
            .orElseThrow(() -> CargoOperationException.cargoNotFound(cargoId));
        
        // Validate status transition
        validationService.validateCustomsStatusTransition(cargo.getCustomsStatus(), newStatus);
        
        cargo.setCustomsStatus(newStatus);
        cargo.setUpdatedAt(LocalDateTime.now());
        
        Cargo updatedCargo = cargoRepository.save(cargo);
        log.info("Successfully updated customs status for cargo ID: {}", cargoId);
        
        return cargoMapper.toResponse(updatedCargo);
    }


    @Transactional(readOnly = true)
    public Page<CargoResponse> getCargoByCustomsStatus(CustomsStatus status, Pageable pageable) {
        log.info("Retrieving cargo by customs status: {}", status);
        
        Page<Cargo> cargoPage = cargoRepository.findByCustomsStatus(status, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<CargoResponse> getCargoByVessel(Long vesselId, Pageable pageable) {
        log.info("Retrieving cargo for vessel: {}", vesselId);
        
        Page<Cargo> cargoPage = cargoRepository.findByVesselId(vesselId, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    

    @Transactional(readOnly = true)
    public Page<CargoResponse> getDangerousCargo(Pageable pageable) {
        log.info("Retrieving dangerous cargo");
        
        Page<Cargo> cargoPage = cargoRepository.findByIsDangerous(true, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    

    @Transactional(readOnly = true)
    public Page<CargoResponse> getCargoByType(CargoType cargoType, Pageable pageable) {
        log.info("Retrieving cargo by type: {}", cargoType);
        
        Page<Cargo> cargoPage = cargoRepository.findByCargoType(cargoType, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    

    @Transactional(readOnly = true)
    public Page<CargoResponse> getCargoByOrigin(String origin, Pageable pageable) {
        log.info("Retrieving cargo by origin: {}", origin);
        
        Page<Cargo> cargoPage = cargoRepository.findByOrigin(origin, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    

    @Transactional(readOnly = true)
    public Page<CargoResponse> getCargoByDestination(String destination, Pageable pageable) {
        log.info("Retrieving cargo by destination: {}", destination);
        
        Page<Cargo> cargoPage = cargoRepository.findByDestination(destination, pageable);
        return cargoPage.map(cargoMapper::toResponse);
    }
    

    @Transactional(readOnly = true)
    public List<CargoResponse> getCargoByWeightRange(BigDecimal minWeight, BigDecimal maxWeight) {
        log.info("Retrieving cargo by weight range: {} - {}", minWeight, maxWeight);
        
        List<Cargo> cargoList = cargoRepository.findByWeightBetween(minWeight, maxWeight);
        return cargoList.stream()
                .map(cargoMapper::toResponse)
                .toList();
    }
    

    @Transactional(readOnly = true)
    public List<CargoResponse> getDangerousCargoCleared() {
        log.info("Retrieving dangerous cargo that is cleared");
        
        List<Cargo> cargoList = cargoRepository.findDangerousCargoCleared();
        return cargoList.stream()
                .map(cargoMapper::toResponse)
                .toList();
    }
    

    @Transactional(readOnly = true)
    public List<CargoResponse> getPendingCargoOrderedByDate() {
        log.info("Retrieving pending cargo ordered by date");
        
        List<Cargo> cargoList = cargoRepository.findPendingCargoOrderedByDate();
        return cargoList.stream()
                .map(cargoMapper::toResponse)
                .toList();
    }
    

    @Transactional(readOnly = true)
    public List<CargoResponse> getDangerousCargoByVessel(Long vesselId) {
        log.info("Retrieving dangerous cargo for vessel: {}", vesselId);
        
        List<Cargo> cargoList = cargoRepository.findDangerousCargoByVessel(vesselId);
        return cargoList.stream()
                .map(cargoMapper::toResponse)
                .toList();
    }
    

    @Transactional(readOnly = true)
    public BigDecimal getTotalWeightByVessel(Long vesselId) {
        log.info("Calculating total weight for vessel: {}", vesselId);
        
        BigDecimal totalWeight = cargoRepository.getTotalWeightByVessel(vesselId);
        return totalWeight != null ? totalWeight : BigDecimal.ZERO;
    }


}