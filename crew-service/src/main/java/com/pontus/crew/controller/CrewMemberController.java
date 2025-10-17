package com.pontus.crew.controller;

import com.pontus.crew.dto.CrewMemberCreateRequest;
import com.pontus.crew.dto.CrewMemberResponse;
import com.pontus.crew.dto.CrewMemberUpdateRequest;
import com.pontus.crew.service.CrewMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.pontus.crew.entity.CrewPosition;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
@Slf4j
// @CrossOrigin(origins = "*") // Commented out - CORS handled by API Gateway
public class CrewMemberController {
    
    private final CrewMemberService crewMemberService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN')")
    public ResponseEntity<CrewMemberResponse> createCrewMember(@Valid @RequestBody CrewMemberCreateRequest request) {
        log.info("Creating new crew member: {}", request.getName());
        CrewMemberResponse response = crewMemberService.createCrewMember(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS')")
    public ResponseEntity<CrewMemberResponse> getCrewMemberById(@PathVariable Long id) {
        log.info("Retrieving crew member with ID: {}", id);
        CrewMemberResponse response = crewMemberService.getCrewMemberById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/license/{licenseNumber}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS')")
    public ResponseEntity<CrewMemberResponse> getCrewMemberByLicenseNumber(@PathVariable String licenseNumber) {
        log.info("Retrieving crew member with license number: {}", licenseNumber);
        CrewMemberResponse response = crewMemberService.getCrewMemberByLicenseNumber(licenseNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CrewMemberResponse>> getAllCrewMembers(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving all crew members");
        Page<CrewMemberResponse> responses = crewMemberService.getAllCrewMembers(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/position/{position}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN') or hasAuthority('OPERATIONS')")
    public ResponseEntity<Page<CrewMemberResponse>> getCrewMembersByPosition(
            @PathVariable String position,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving crew members by position: {}", position);
        Page<CrewMemberResponse> responses = crewMemberService.getCrewMembersByPosition(
            CrewPosition.valueOf(position.toUpperCase()), pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/certificates/expiring")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Page<CrewMemberResponse>> getCrewMembersWithExpiringCertificates(
            @RequestParam(defaultValue = "30") int days,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("Retrieving crew members with certificates expiring in {} days", days);
        Page<CrewMemberResponse> responses = crewMemberService.getCrewMembersWithExpiringCertificates(days, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('VESSEL_CAPTAIN')")
    public ResponseEntity<CrewMemberResponse> updateCrewMember(@PathVariable Long id, 
                                                              @Valid @RequestBody CrewMemberUpdateRequest request) {
        log.info("Updating crew member with ID: {}", id);
        CrewMemberResponse response = crewMemberService.updateCrewMember(id, request);
        return ResponseEntity.ok(response);
    }
    

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteCrewMember(@PathVariable Long id) {
        log.info("Deleting crew member with ID: {}", id);
        crewMemberService.deleteCrewMember(id);
        return ResponseEntity.noContent().build();
    }
    
}
