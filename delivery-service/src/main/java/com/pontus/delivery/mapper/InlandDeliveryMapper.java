package com.pontus.delivery.mapper;

import com.pontus.delivery.dto.InlandDeliveryCreateRequest;
import com.pontus.delivery.dto.InlandDeliveryResponse;
import com.pontus.delivery.dto.InlandDeliveryUpdateRequest;
import com.pontus.delivery.entity.InlandDelivery;
import org.springframework.stereotype.Component;

@Component
public class InlandDeliveryMapper {
    
    public InlandDelivery toEntity(InlandDeliveryCreateRequest request) {
        InlandDelivery delivery = new InlandDelivery();
        delivery.setCargoId(request.getCargoId());
        delivery.setDestinationAddress(request.getDestinationAddress());
        delivery.setDeliveryStatus(request.getDeliveryStatus());
        delivery.setVehicleRegistration(request.getVehicleRegistration());
        delivery.setDriverName(request.getDriverName());
        delivery.setScheduledDeliveryTime(request.getScheduledDeliveryTime());
        delivery.setNotes(request.getNotes());
        return delivery;
    }
    
    public InlandDeliveryResponse toResponse(InlandDelivery delivery) {
        InlandDeliveryResponse response = new InlandDeliveryResponse();
        response.setId(delivery.getId());
        response.setCargoId(delivery.getCargoId());
        response.setDestinationAddress(delivery.getDestinationAddress());
        response.setDeliveryStatus(delivery.getDeliveryStatus());
        response.setVehicleRegistration(delivery.getVehicleRegistration());
        response.setDriverName(delivery.getDriverName());
        response.setDeliveryTime(delivery.getDeliveryTime());
        response.setScheduledDeliveryTime(delivery.getScheduledDeliveryTime());
        response.setNotes(delivery.getNotes());
        response.setCreatedAt(delivery.getCreatedAt());
        response.setUpdatedAt(delivery.getUpdatedAt());
        return response;
    }
    
    public void updateEntity(InlandDelivery delivery, InlandDeliveryUpdateRequest request) {
        delivery.setCargoId(request.getCargoId());
        delivery.setDestinationAddress(request.getDestinationAddress());
        delivery.setDeliveryStatus(request.getDeliveryStatus());
        delivery.setVehicleRegistration(request.getVehicleRegistration());
        delivery.setDriverName(request.getDriverName());
        delivery.setDeliveryTime(request.getDeliveryTime());
        delivery.setScheduledDeliveryTime(request.getScheduledDeliveryTime());
        delivery.setNotes(request.getNotes());
    }
}
