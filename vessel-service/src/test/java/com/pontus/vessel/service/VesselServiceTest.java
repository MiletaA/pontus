// package com.pontus.vessel.service;

// import com.pontus.vessel.dto.VesselCreateRequest;
// import com.pontus.vessel.dto.VesselResponse;
// import com.pontus.vessel.dto.VesselUpdateRequest;
// import com.pontus.vessel.entity.Vessel;
// import com.pontus.vessel.exception.DuplicateImoNumberException;
// import com.pontus.vessel.exception.InvalidVesselDataException;
// import com.pontus.vessel.exception.VesselNotFoundException;
// import com.pontus.vessel.repository.VesselRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class VesselServiceTest {

//     @Mock
//     private VesselRepository vesselRepository;

//     @InjectMocks
//     private VesselService vesselService;

//     private VesselCreateRequest createRequest;
//     private Vessel vessel;
//     private VesselResponse expectedResponse;

//     @BeforeEach
//     void setUp() {
//         createRequest = new VesselCreateRequest();
//         createRequest.setName("Test Vessel");
//         createRequest.setImoNumber("IMO1234567");
//         createRequest.setVesselType(VesselType.CARGO_SHIP);
//         createRequest.setLength(new BigDecimal("150.50"));
//         createRequest.setFlagCountry("Norway");
//         createRequest.setStatus("SCHEDULED");
//         createRequest.setScheduledArrival(LocalDateTime.now().plusDays(1));
//         createRequest.setScheduledDeparture(LocalDateTime.now().plusDays(2));

//         vessel = new Vessel();
//         vessel.setId(1L);
//         vessel.setName("Test Vessel");
//         vessel.setImoNumber("IMO1234567");
//         vessel.setVesselType(VesselType.CARGO_SHIP);
//         vessel.setLength(new BigDecimal("150.50"));
//         vessel.setFlagCountry("Norway");
//         vessel.setStatus("SCHEDULED");
//         vessel.setScheduledArrival(LocalDateTime.now().plusDays(1));
//         vessel.setScheduledDeparture(LocalDateTime.now().plusDays(2));

//         expectedResponse = new VesselResponse();
//         expectedResponse.setId(1L);
//         expectedResponse.setName("Test Vessel");
//         expectedResponse.setImoNumber("IMO1234567");
//         expectedResponse.setVesselType(VesselType.CARGO_SHIP);
//         expectedResponse.setLength(new BigDecimal("150.50"));
//         expectedResponse.setFlagCountry("Norway");
//         expectedResponse.setStatus("SCHEDULED");
//     }

//     @Test
//     void createVessel_Success() {
//         // Given
//         when(vesselRepository.existsByImoNumber(anyString())).thenReturn(false);
//         when(vesselRepository.save(any(Vessel.class))).thenReturn(vessel);

//         // When
//         VesselResponse result = vesselService.createVessel(createRequest);

//         // Then
//         assertNotNull(result);
//         assertEquals("Test Vessel", result.getName());
//         assertEquals("IMO1234567", result.getImoNumber());
//         verify(vesselRepository).existsByImoNumber("IMO1234567");
//         verify(vesselRepository).save(any(Vessel.class));
//     }

//     @Test
//     void createVessel_DuplicateImoNumber_ThrowsException() {
//         // Given
//         when(vesselRepository.existsByImoNumber(anyString())).thenReturn(true);

//         // When & Then
//         assertThrows(DuplicateImoNumberException.class, () -> {
//             vesselService.createVessel(createRequest);
//         });
//         verify(vesselRepository).existsByImoNumber("IMO1234567");
//         verify(vesselRepository, never()).save(any(Vessel.class));
//     }

//     @Test
//     void createVessel_InvalidDates_ThrowsException() {
//         // Given
//         createRequest.setScheduledArrival(LocalDateTime.now().plusDays(2));
//         createRequest.setScheduledDeparture(LocalDateTime.now().plusDays(1)); // Before arrival

//         // When & Then
//         assertThrows(InvalidVesselDataException.class, () -> {
//             vesselService.createVessel(createRequest);
//         });
//     }

//     @Test
//     void createVessel_InvalidStatus_ThrowsException() {
//         // Given
//         createRequest.setStatus("INVALID_STATUS");

//         // When & Then
//         assertThrows(InvalidVesselDataException.class, () -> {
//             vesselService.createVessel(createRequest);
//         });
//     }

//     @Test
//     void getVesselById_Success() {
//         // Given
//         when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));

//         // When
//         VesselResponse result = vesselService.getVesselById(1L);

//         // Then
//         assertNotNull(result);
//         assertEquals(1L, result.getId());
//         assertEquals("Test Vessel", result.getName());
//         verify(vesselRepository).findById(1L);
//     }

//     @Test
//     void getVesselById_NotFound_ThrowsException() {
//         // Given
//         when(vesselRepository.findById(1L)).thenReturn(Optional.empty());

//         // When & Then
//         assertThrows(VesselNotFoundException.class, () -> {
//             vesselService.getVesselById(1L);
//         });
//         verify(vesselRepository).findById(1L);
//     }

//     @Test
//     void getAllVessels_Success() {
//         // Given
//         List<Vessel> vessels = Arrays.asList(vessel);
//         when(vesselRepository.findAll()).thenReturn(vessels);

//         // When
//         List<VesselResponse> results = vesselService.getAllVessels();

//         // Then
//         assertNotNull(results);
//         assertEquals(1, results.size());
//         assertEquals("Test Vessel", results.get(0).getName());
//         verify(vesselRepository).findAll();
//     }

//     @Test
//     void updateVessel_Success() {
//         // Given
//         VesselUpdateRequest updateRequest = new VesselUpdateRequest();
//         updateRequest.setName("Updated Vessel");
//         updateRequest.setStatus("ARRIVED");

//         when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));
//         when(vesselRepository.save(any(Vessel.class))).thenReturn(vessel);

//         // When
//         VesselResponse result = vesselService.updateVessel(1L, updateRequest);

//         // Then
//         assertNotNull(result);
//         verify(vesselRepository).findById(1L);
//         verify(vesselRepository).save(any(Vessel.class));
//     }

//     @Test
//     void deleteVessel_Success() {
//         // Given
//         when(vesselRepository.existsById(1L)).thenReturn(true);

//         // When
//         vesselService.deleteVessel(1L);

//         // Then
//         verify(vesselRepository).existsById(1L);
//         verify(vesselRepository).deleteById(1L);
//     }

//     @Test
//     void deleteVessel_NotFound_ThrowsException() {
//         // Given
//         when(vesselRepository.existsById(1L)).thenReturn(false);

//         // When & Then
//         assertThrows(VesselNotFoundException.class, () -> {
//             vesselService.deleteVessel(1L);
//         });
//         verify(vesselRepository).existsById(1L);
//         verify(vesselRepository, never()).deleteById(1L);
//     }

//     @Test
//     void updateVesselArrival_Success() {
//         // Given
//         LocalDateTime arrivalTime = LocalDateTime.now();
//         when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));
//         when(vesselRepository.save(any(Vessel.class))).thenReturn(vessel);

//         // When
//         VesselResponse result = vesselService.updateVesselArrival(1L, arrivalTime);

//         // Then
//         assertNotNull(result);
//         verify(vesselRepository).findById(1L);
//         verify(vesselRepository).save(any(Vessel.class));
//     }

//     @Test
//     void updateVesselDeparture_Success() {
//         // Given
//         LocalDateTime departureTime = LocalDateTime.now();
//         vessel.setActualArrival(LocalDateTime.now().minusHours(2)); // Set arrival before departure
//         when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));
//         when(vesselRepository.save(any(Vessel.class))).thenReturn(vessel);

//         // When
//         VesselResponse result = vesselService.updateVesselDeparture(1L, departureTime);

//         // Then
//         assertNotNull(result);
//         verify(vesselRepository).findById(1L);
//         verify(vesselRepository).save(any(Vessel.class));
//     }
// }
