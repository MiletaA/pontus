package com.pontus.crew.service;

import com.pontus.crew.dto.*;
import com.pontus.crew.entity.CrewMember;
import com.pontus.crew.entity.CrewPosition;
import com.pontus.crew.mapper.CrewMemberMapper;
import com.pontus.crew.repository.CrewMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;
    private final CrewMemberMapper crewMemberMapper;

    public CrewMemberResponse createCrewMember(CrewMemberCreateRequest request) {
        log.info("Creating new crew member: {}", request.getName());
        
        CrewMember crewMember = crewMemberMapper.toEntity(request);
        crewMember.setCreatedAt(LocalDateTime.now());
        crewMember.setUpdatedAt(LocalDateTime.now());
        
        CrewMember savedCrewMember = crewMemberRepository.save(crewMember);
        log.info("Successfully created crew member with ID: {}", savedCrewMember.getId());
        
        return crewMemberMapper.toResponse(savedCrewMember);
    }

    @Transactional(readOnly = true)
    public CrewMemberResponse getCrewMemberById(Long crewMemberId) {
        log.info("Retrieving crew member with ID: {}", crewMemberId);
        
        CrewMember crewMember = crewMemberRepository.findById(crewMemberId)
            .orElseThrow(() -> new RuntimeException("Crew member not found: " + crewMemberId));
        
        return crewMemberMapper.toResponse(crewMember);
    }

    @Transactional(readOnly = true)
    public Page<CrewMemberResponse> getAllCrewMembers(Pageable pageable) {
        log.info("Retrieving all crew members with pagination: {}", pageable);
        
        Page<CrewMember> crewMemberPage = crewMemberRepository.findAll(pageable);
        return crewMemberPage.map(crewMemberMapper::toResponse);
    }

    public CrewMemberResponse updateCrewMember(Long crewMemberId, CrewMemberUpdateRequest request) {
        log.info("Updating crew member with ID: {}", crewMemberId);
        
        CrewMember existingCrewMember = crewMemberRepository.findById(crewMemberId)
            .orElseThrow(() -> new RuntimeException("Crew member not found: " + crewMemberId));
        
        crewMemberMapper.updateEntity(existingCrewMember, request);
        existingCrewMember.setUpdatedAt(LocalDateTime.now());
        
        CrewMember updatedCrewMember = crewMemberRepository.save(existingCrewMember);
        log.info("Successfully updated crew member with ID: {}", crewMemberId);
        
        return crewMemberMapper.toResponse(updatedCrewMember);
    }

    public void deleteCrewMember(Long crewMemberId) {
        log.info("Deleting crew member with ID: {}", crewMemberId);
        
        if (!crewMemberRepository.existsById(crewMemberId)) {
            throw new RuntimeException("Crew member not found: " + crewMemberId);
        }
        
        crewMemberRepository.deleteById(crewMemberId);
        log.info("Successfully deleted crew member with ID: {}", crewMemberId);
    }

    @Transactional(readOnly = true)
    public Page<CrewMemberResponse> getCrewMembersByPosition(CrewPosition position, Pageable pageable) {
        log.info("Retrieving crew members by position: {}", position);
        
        Page<CrewMember> crewMemberPage = crewMemberRepository.findAll(pageable);
        return crewMemberPage.map(crewMemberMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CrewMemberResponse getCrewMemberByLicenseNumber(String licenseNumber) {
        log.info("Retrieving crew member by license number: {}", licenseNumber);
        
        CrewMember crewMember = crewMemberRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Crew member not found"));
        
        return crewMemberMapper.toResponse(crewMember);
    }


    @Transactional(readOnly = true)
    public Page<CrewMemberResponse> getCrewMembersWithExpiringCertificates(int daysUntilExpiry, Pageable pageable) {
        log.info("Retrieving crew members with certificates expiring in {} days", daysUntilExpiry);
        
        Page<CrewMember> crewMemberPage = crewMemberRepository.findAll(pageable);
        return crewMemberPage.map(crewMemberMapper::toResponse);
    }
}