package com.pontus.cargo.mapper;

import com.pontus.cargo.dto.CargoCreateRequest;
import com.pontus.cargo.dto.CargoResponse;
import com.pontus.cargo.dto.CargoUpdateRequest;
import com.pontus.cargo.entity.Cargo;
import org.springframework.stereotype.Component;

@Component
public class CargoMapper {
    
    public Cargo toEntity(CargoCreateRequest request) {
        Cargo cargo = new Cargo();
        cargo.setVesselId(request.getVesselId());
        cargo.setDescription(request.getDescription());
        cargo.setWeightTons(request.getWeightTons());
        cargo.setIsDangerous(request.getIsDangerous());
        cargo.setCustomsStatus(request.getCustomsStatus());
        cargo.setCargoType(request.getCargoType());
        cargo.setOrigin(request.getOrigin());
        cargo.setDestination(request.getDestination());
        return cargo;
    }
    
    public CargoResponse toResponse(Cargo cargo) {
        CargoResponse response = new CargoResponse();
        response.setId(cargo.getId());
        response.setVesselId(cargo.getVesselId());
        response.setDescription(cargo.getDescription());
        response.setWeightTons(cargo.getWeightTons());
        response.setIsDangerous(cargo.getIsDangerous());
        response.setCustomsStatus(cargo.getCustomsStatus());
        response.setCargoType(cargo.getCargoType());
        response.setOrigin(cargo.getOrigin());
        response.setDestination(cargo.getDestination());
        response.setCreatedAt(cargo.getCreatedAt());
        response.setUpdatedAt(cargo.getUpdatedAt());
        return response;
    }
    
    public void updateEntity(Cargo cargo, CargoUpdateRequest request) {
        cargo.setVesselId(request.getVesselId());
        cargo.setDescription(request.getDescription());
        cargo.setWeightTons(request.getWeightTons());
        cargo.setIsDangerous(request.getIsDangerous());
        cargo.setCustomsStatus(request.getCustomsStatus());
        cargo.setCargoType(request.getCargoType());
        cargo.setOrigin(request.getOrigin());
        cargo.setDestination(request.getDestination());
    }
}
