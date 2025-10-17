package com.pontus.crew.mapper;

import com.pontus.crew.dto.CrewMemberCreateRequest;
import com.pontus.crew.dto.CrewMemberResponse;
import com.pontus.crew.dto.CrewMemberUpdateRequest;
import com.pontus.crew.entity.CrewMember;
import org.springframework.stereotype.Component;

@Component
public class CrewMemberMapper {
    
    public CrewMember toEntity(CrewMemberCreateRequest request) {
        CrewMember crewMember = new CrewMember();
        crewMember.setVesselId(request.getVesselId());
        crewMember.setName(request.getName());
        crewMember.setNationality(request.getNationality());
        crewMember.setPosition(request.getPosition());
        crewMember.setDateOfBirth(request.getDateOfBirth());
        crewMember.setPassportNumber(request.getPassportNumber());
        crewMember.setCertificate(request.getCertificate());
        crewMember.setCertificateExpiry(request.getCertificateExpiry());
        return crewMember;
    }
    
    public CrewMemberResponse toResponse(CrewMember crewMember) {
        CrewMemberResponse response = new CrewMemberResponse();
        response.setId(crewMember.getId());
        response.setVesselId(crewMember.getVesselId());
        response.setName(crewMember.getName());
        response.setNationality(crewMember.getNationality());
        response.setPosition(crewMember.getPosition());
        response.setDateOfBirth(crewMember.getDateOfBirth());
        response.setPassportNumber(crewMember.getPassportNumber());
        response.setCertificate(crewMember.getCertificate());
        response.setCertificateExpiry(crewMember.getCertificateExpiry());
        response.setCreatedAt(crewMember.getCreatedAt());
        response.setUpdatedAt(crewMember.getUpdatedAt());
        return response;
    }
    
    public void updateEntity(CrewMember crewMember, CrewMemberUpdateRequest request) {
        crewMember.setVesselId(request.getVesselId());
        crewMember.setName(request.getName());
        crewMember.setNationality(request.getNationality());
        crewMember.setPosition(request.getPosition());
        crewMember.setDateOfBirth(request.getDateOfBirth());
        crewMember.setPassportNumber(request.getPassportNumber());
        crewMember.setCertificate(request.getCertificate());
        crewMember.setCertificateExpiry(request.getCertificateExpiry());
    }
}
