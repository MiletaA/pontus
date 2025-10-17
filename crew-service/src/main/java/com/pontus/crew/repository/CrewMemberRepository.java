package com.pontus.crew.repository;

import com.pontus.crew.entity.CrewMember;
import com.pontus.crew.entity.CrewPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    
    List<CrewMember> findByVesselId(Long vesselId);
    
    List<CrewMember> findByNationality(String nationality);
    
    List<CrewMember> findByPosition(CrewPosition position);
    
    Optional<CrewMember> findByPassportNumber(String passportNumber);
    
    List<CrewMember> findByName(String name);
    
    @Query("SELECT cm FROM CrewMember cm WHERE cm.certificateExpiry IS NOT NULL AND cm.certificateExpiry < :date")
    List<CrewMember> findByExpiredCertificates(@Param("date") LocalDate date);
    
    @Query("SELECT cm FROM CrewMember cm WHERE cm.certificateExpiry IS NOT NULL AND cm.certificateExpiry BETWEEN :startDate AND :endDate")
    List<CrewMember> findByExpiringCertificates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT cm FROM CrewMember cm WHERE cm.certificate IS NOT NULL AND cm.certificate != ''")
    List<CrewMember> findCrewMembersWithCertificates();
    
    @Query("SELECT cm FROM CrewMember cm WHERE cm.certificate IS NULL OR cm.certificate = ''")
    List<CrewMember> findCrewMembersWithoutCertificates();
    
    @Query("SELECT COUNT(cm) FROM CrewMember cm WHERE cm.vesselId = :vesselId")
    long countByVesselId(@Param("vesselId") Long vesselId);
    
    @Query("SELECT COUNT(cm) FROM CrewMember cm WHERE cm.nationality = :nationality")
    long countByNationality(@Param("nationality") String nationality);
    
    @Query("SELECT COUNT(cm) FROM CrewMember cm WHERE cm.position = :position")
    long countByPosition(@Param("position") CrewPosition position);
    
    @Query("SELECT DISTINCT cm.nationality FROM CrewMember cm ORDER BY cm.nationality")
    List<String> findDistinctNationalities();
    
    @Query("SELECT DISTINCT cm.position FROM CrewMember cm ORDER BY cm.position")
    List<String> findDistinctRanks();
    
    @Query("SELECT cm FROM CrewMember cm WHERE cm.dateOfBirth BETWEEN :startDate AND :endDate")
    List<CrewMember> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
