package com.pontus.vessel.dto;

import com.pontus.vessel.enums.VesselStatus;
import com.pontus.vessel.enums.VesselType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VesselResponse {
    
    private Long id;
    private String name;
    private String imoNumber;
    private VesselType vesselType;
    private BigDecimal length;
    private String flagCountry;
    private VesselStatus status;
    private LocalDateTime scheduledArrival;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualArrival;
    private LocalDateTime actualDeparture;
}
