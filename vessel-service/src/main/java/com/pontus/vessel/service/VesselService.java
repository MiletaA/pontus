package com.pontus.vessel.service;

import com.pontus.vessel.dto.VesselCreateRequest;
import com.pontus.vessel.dto.VesselResponse;
import com.pontus.vessel.dto.VesselUpdateRequest;
import com.pontus.vessel.entity.Vessel;
import com.pontus.vessel.enums.VesselType;
import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.exception.VesselOperationException;
import com.pontus.vessel.mapper.VesselMapper;
import com.pontus.vessel.repository.VesselRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for vessel operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VesselService {

    private final VesselRepository vesselRepository;
    private final VesselValidationService validationService;
    private final VesselMapper vesselMapper;


    public VesselResponse createVessel(VesselCreateRequest request) {
        log.info("Creating new vessel with IMO number: {}", request.getImoNumber());

        validationService.validateImoNumberFormat(request.getImoNumber());

        if (vesselRepository.existsByImoNumber(request.getImoNumber())) {
            throw VesselOperationException.duplicateImoNumber(request.getImoNumber());
        }

        validationService.validateDates(request.getScheduledArrival(), request.getScheduledDeparture(),
                request.getActualArrival(), request.getActualDeparture());
        validationService.validateStatus(request.getStatus());

        Vessel vessel = vesselMapper.toEntity(request);
        Vessel savedVessel = vesselRepository.save(vessel);

        log.info("Successfully created vessel with ID: {}", savedVessel.getId());
        return vesselMapper.toResponse(savedVessel);
    }


    @Transactional(readOnly = true)
    public VesselResponse getVesselById(Long id) {
        log.info("Retrieving vessel with ID: {}", id);
        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> VesselOperationException.vesselNotFound(id));
        return vesselMapper.toResponse(vessel);
    }

    @Transactional(readOnly = true)
    public VesselResponse getVesselByImoNumber(String imoNumber) {
        log.info("Retrieving vessel with IMO number: {}", imoNumber);
        Vessel vessel = vesselRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> VesselOperationException.vesselNotFound(imoNumber));
        return vesselMapper.toResponse(vessel);
    }

    @Transactional(readOnly = true)
    public Page<VesselResponse> getAllVessels(Pageable pageable) {
        log.info("Retrieving all vessels");
        Page<Vessel> vesselPage = vesselRepository.findAll(pageable);
        return vesselPage.map(vesselMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<VesselResponse> getVesselsByStatus(VesselStatus status, Pageable pageable) {
        log.info("Retrieving vessels with status: {}", status);
        validateStatus(status);
        Page<Vessel> vesselPage = vesselRepository.findByStatus(status, pageable);
        return vesselPage.map(vesselMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<VesselResponse> getVesselsByType(VesselType vesselType, Pageable pageable) {
        log.info("Retrieving vessels with type: {}", vesselType);
        Page<Vessel> vesselPage = vesselRepository.findByVesselType(vesselType, pageable);
        return vesselPage.map(vesselMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<VesselResponse> getVesselsExpectedToArrive() {
        log.info("Retrieving vessels expected to arrive");
        return vesselRepository.findVesselsExpectedToArrive().stream()
                .map(vesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VesselResponse> getVesselsExpectedToDepart() {
        log.info("Retrieving vessels expected to depart");
        return vesselRepository.findVesselsExpectedToDepart().stream()
                .map(vesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VesselResponse> getVesselsCurrentlyInPort() {
        log.info("Retrieving vessels currently in port");
        return vesselRepository.findVesselsCurrentlyInPort().stream()
                .map(vesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VesselResponse updateVessel(Long id, VesselUpdateRequest request) {
        log.info("Updating vessel with ID: {}", id);

        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> VesselOperationException.vesselNotFound(id));

        // Check IMO number uniqueness if it's being updated
        if (request.getImoNumber() != null && !request.getImoNumber().equals(vessel.getImoNumber())) {
            if (vesselRepository.existsByImoNumber(request.getImoNumber())) {
                throw VesselOperationException.duplicateImoNumber(request.getImoNumber());
            }
        }

        // Validate dates
        LocalDateTime scheduledArrival = request.getScheduledArrival() != null ? request.getScheduledArrival()
                : vessel.getScheduledArrival();
        LocalDateTime scheduledDeparture = request.getScheduledDeparture() != null ? request.getScheduledDeparture()
                : vessel.getScheduledDeparture();
        LocalDateTime actualArrival = request.getActualArrival() != null ? request.getActualArrival()
                : vessel.getActualArrival();
        LocalDateTime actualDeparture = request.getActualDeparture() != null ? request.getActualDeparture()
                : vessel.getActualDeparture();

        validateDates(scheduledArrival, scheduledDeparture, actualArrival, actualDeparture);

        // Validate status if provided
        if (request.getStatus() != null) {
            validateStatus(request.getStatus());
        }

        vesselMapper.updateEntity(vessel, request);
        Vessel updatedVessel = vesselRepository.save(vessel);

        log.info("Successfully updated vessel with ID: {}", updatedVessel.getId());
        return vesselMapper.toResponse(updatedVessel);
    }

    public void deleteVessel(Long id) {
        log.info("Deleting vessel with ID: {}", id);

        if (!vesselRepository.existsById(id)) {
            throw VesselOperationException.vesselNotFound(id);
        }

        vesselRepository.deleteById(id);
        log.info("Successfully deleted vessel with ID: {}", id);
    }

    public VesselResponse updateVesselArrival(Long id, LocalDateTime actualArrival) {
        log.info("Updating actual arrival for vessel with ID: {}", id);

        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> VesselOperationException.vesselNotFound(id));

        if (actualArrival != null && vessel.getActualDeparture() != null &&
                actualArrival.isAfter(vessel.getActualDeparture())) {
            throw VesselOperationException.invalidDateRange(actualArrival, vessel.getActualDeparture(), "Actual arrival cannot be after actual departure");
        }

        vessel.setActualArrival(actualArrival);
        vessel.setStatus(VesselStatus.BERTHED);

        Vessel updatedVessel = vesselRepository.save(vessel);
        log.info("Successfully updated arrival for vessel with ID: {}", updatedVessel.getId());
        return vesselMapper.toResponse(updatedVessel);
    }

    public VesselResponse updateVesselDeparture(Long id, LocalDateTime actualDeparture) {
        log.info("Updating actual departure for vessel with ID: {}", id);

        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> VesselOperationException.vesselNotFound(id));

        if (actualDeparture != null && vessel.getActualArrival() != null &&
                actualDeparture.isBefore(vessel.getActualArrival())) {
            throw VesselOperationException.invalidDateRange(vessel.getActualArrival(), actualDeparture, "Actual departure cannot be before actual arrival");
        }

        vessel.setActualDeparture(actualDeparture);
        vessel.setStatus(VesselStatus.DEPARTED);

        Vessel updatedVessel = vesselRepository.save(vessel);
        log.info("Successfully updated departure for vessel with ID: {}", updatedVessel.getId());
        return vesselMapper.toResponse(updatedVessel);
    }

    private void validateDates(LocalDateTime scheduledArrival, LocalDateTime scheduledDeparture,
            LocalDateTime actualArrival, LocalDateTime actualDeparture) {
        if (scheduledArrival != null && scheduledDeparture != null) {
            if (scheduledDeparture.isBefore(scheduledArrival)) {
                throw VesselOperationException.invalidDateRange(scheduledArrival, scheduledDeparture, "Scheduled departure cannot be before scheduled arrival");
            }
        }

        if (actualArrival != null && actualDeparture != null) {
            if (actualDeparture.isBefore(actualArrival)) {
                throw VesselOperationException.invalidDateRange(actualArrival, actualDeparture, "Actual departure cannot be before actual arrival");
            }
        }
    }

    private void validateStatus(VesselStatus status) {
        if (status == null) {
            throw VesselOperationException.invalidVesselStatus("null", "Status cannot be null");
        }
        // Note: VesselStatus enum provides type safety, so no additional validation needed
        // All enum values are valid by definition
    }


}
