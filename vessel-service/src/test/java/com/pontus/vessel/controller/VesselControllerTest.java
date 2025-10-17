// package com.pontus.vessel.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.pontus.vessel.dto.VesselCreateRequest;
// import com.pontus.vessel.dto.VesselResponse;
// import com.pontus.vessel.dto.VesselUpdateRequest;
// import com.pontus.vessel.exception.VesselNotFoundException;
// import com.pontus.vessel.service.VesselService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(VesselController.class)
// class VesselControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private VesselService vesselService;

//     private ObjectMapper objectMapper;
//     private VesselCreateRequest createRequest;
//     private VesselResponse vesselResponse;

//     @BeforeEach
//     void setUp() {
//         objectMapper = new ObjectMapper();
//         objectMapper.registerModule(new JavaTimeModule());

//         createRequest = new VesselCreateRequest();
//         createRequest.setName("Test Vessel");
//         createRequest.setImoNumber("IMO1234567");
//         createRequest.setVesselType(VesselType.CARGO_SHIP);
//         createRequest.setLength(new BigDecimal("150.50"));
//         createRequest.setFlagCountry("Norway");
//         createRequest.setStatus("SCHEDULED");
//         createRequest.setScheduledArrival(LocalDateTime.now().plusDays(1));
//         createRequest.setScheduledDeparture(LocalDateTime.now().plusDays(2));

//         vesselResponse = new VesselResponse();
//         vesselResponse.setId(1L);
//         vesselResponse.setName("Test Vessel");
//         vesselResponse.setImoNumber("IMO1234567");
//         vesselResponse.setVesselType(VesselType.CARGO_SHIP);
//         vesselResponse.setLength(new BigDecimal("150.50"));
//         vesselResponse.setFlagCountry("Norway");
//         vesselResponse.setStatus("SCHEDULED");
//     }

//     @Test
//     void createVessel_Success() throws Exception {
//         // Given
//         when(vesselService.createVessel(any(VesselCreateRequest.class))).thenReturn(vesselResponse);

//         // When & Then
//         mockMvc.perform(post("/api/vessels")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(createRequest)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.name").value("Test Vessel"))
//                 .andExpect(jsonPath("$.imoNumber").value("IMO1234567"));
//     }

//     @Test
//     void createVessel_InvalidRequest_BadRequest() throws Exception {
//         // Given
//         createRequest.setName(""); // Invalid name

//         // When & Then
//         mockMvc.perform(post("/api/vessels")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(createRequest)))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     void getVesselById_Success() throws Exception {
//         // Given
//         when(vesselService.getVesselById(1L)).thenReturn(vesselResponse);

//         // When & Then
//         mockMvc.perform(get("/api/vessels/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.name").value("Test Vessel"));
//     }

//     @Test
//     void getVesselById_NotFound() throws Exception {
//         // Given
//         when(vesselService.getVesselById(1L)).thenThrow(new VesselNotFoundException("Vessel not found"));

//         // When & Then
//         mockMvc.perform(get("/api/vessels/1"))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     void getAllVessels_Success() throws Exception {
//         // Given
//         List<VesselResponse> vessels = Arrays.asList(vesselResponse);
//         when(vesselService.getAllVessels()).thenReturn(vessels);

//         // When & Then
//         mockMvc.perform(get("/api/vessels"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$").isArray())
//                 .andExpect(jsonPath("$[0].id").value(1L))
//                 .andExpect(jsonPath("$[0].name").value("Test Vessel"));
//     }

//     @Test
//     void updateVessel_Success() throws Exception {
//         // Given
//         VesselUpdateRequest updateRequest = new VesselUpdateRequest();
//         updateRequest.setName("Updated Vessel");
//         updateRequest.setStatus("ARRIVED");

//         VesselResponse updatedResponse = new VesselResponse();
//         updatedResponse.setId(1L);
//         updatedResponse.setName("Updated Vessel");
//         updatedResponse.setStatus("ARRIVED");

//         when(vesselService.updateVessel(eq(1L), any(VesselUpdateRequest.class))).thenReturn(updatedResponse);

//         // When & Then
//         mockMvc.perform(put("/api/vessels/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(updateRequest)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.name").value("Updated Vessel"))
//                 .andExpect(jsonPath("$.status").value("ARRIVED"));
//     }

//     @Test
//     void deleteVessel_Success() throws Exception {
//         // When & Then
//         mockMvc.perform(delete("/api/vessels/1"))
//                 .andExpect(status().isNoContent());
//     }

//     @Test
//     void getVesselsByStatus_Success() throws Exception {
//         // Given
//         List<VesselResponse> vessels = Arrays.asList(vesselResponse);
//         when(vesselService.getVesselsByStatus("SCHEDULED")).thenReturn(vessels);

//         // When & Then
//         mockMvc.perform(get("/api/vessels/status/SCHEDULED"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$").isArray())
//                 .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
//     }

//     @Test
//     void getVesselsExpectedToArrive_Success() throws Exception {
//         // Given
//         List<VesselResponse> vessels = Arrays.asList(vesselResponse);
//         when(vesselService.getVesselsExpectedToArrive()).thenReturn(vessels);

//         // When & Then
//         mockMvc.perform(get("/api/vessels/expected-arrivals"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$").isArray());
//     }

//     @Test
//     void healthCheck_Success() throws Exception {
//         // When & Then
//         mockMvc.perform(get("/api/vessels/health"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Vessel Service is running"));
//     }
// }
