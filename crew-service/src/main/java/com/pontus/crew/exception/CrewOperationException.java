package com.pontus.crew.exception;

import java.time.LocalDate;

public class CrewOperationException extends RuntimeException {
    
    public CrewOperationException(String message) {
        super(message);
    }
    
    public CrewOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static CrewOperationException certificateExpired(Long crewMemberId, LocalDate expiryDate) {
        return new CrewOperationException("Crew member " + crewMemberId + " has expired certificate. Expired on: " + expiryDate);
    }
    
    public static CrewOperationException certificateExpiringSoon(Long crewMemberId, LocalDate expiryDate) {
        return new CrewOperationException("Crew member " + crewMemberId + " certificate expires soon on: " + expiryDate);
    }
    
    public static CrewOperationException vesselCapacityExceeded(Long vesselId, int currentCrew, int maxCapacity) {
        return new CrewOperationException("Adding crew member would exceed vessel " + vesselId + " capacity. Current: " + currentCrew + ", Max: " + maxCapacity);
    }
    
    public static CrewOperationException invalidAge(String name, int age, int minAge) {
        return new CrewOperationException("Crew member " + name + " is too young. Age: " + age + ", Minimum required: " + minAge);
    }
    
    public static CrewOperationException invalidAge(String name, int age, int minAge, String message) {
        return new CrewOperationException("Crew member " + name + " age validation failed. Age: " + age + ", Required: " + minAge + ". " + message);
    }
    
    public static CrewOperationException certificateExpired(Long crewMemberId, LocalDate expiryDate, String message) {
        if (crewMemberId != null) {
            return new CrewOperationException("Crew member " + crewMemberId + " certificate issue: " + message + " (Expired: " + expiryDate + ")");
        }
        return new CrewOperationException("Certificate issue: " + message);
    }
    
    public static CrewOperationException invalidRank(String message) {
        return new CrewOperationException("Invalid crew rank: " + message);
    }
    
    public static CrewOperationException invalidPassportNumber(String message) {
        return new CrewOperationException("Invalid passport number: " + message);
    }
    
    public static CrewOperationException invalidCrewData(String message) {
        return new CrewOperationException("Invalid crew data: " + message);
    }
}
