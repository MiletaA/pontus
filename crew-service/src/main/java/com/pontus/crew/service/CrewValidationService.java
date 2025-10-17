package com.pontus.crew.service;

import com.pontus.crew.entity.CrewPosition;
import com.pontus.crew.exception.CrewOperationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service responsible for validating crew member-related data.
 * 
 * This service follows the Single Responsibility Principle by focusing solely on validation logic.
 * It can be easily extended with new validation rules without modifying existing code (Open/Closed Principle).
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class CrewValidationService {
    

    
    /**
     * Minimum age requirement for crew members (maritime regulations).
     */
    private static final int MINIMUM_AGE = 18;
    
    /**
     * Maximum age for active crew service.
     */
    private static final int MAXIMUM_AGE = 65;
    
    /**
     * Certificate warning period in days.
     */
    private static final int CERTIFICATE_WARNING_DAYS = 30;
    
    /**
     * Pattern for passport number validation (basic international format).
     */
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");
    
    /**
     * Validates crew member age according to maritime regulations.
     * 
     * Business Rules:
     * - Minimum age is 18 years (international maritime law)
     * - Maximum age is 65 years for active service
     * - Age calculation based on current date
     * 
     * @param name The crew member's name (for error messages)
     * @param dateOfBirth The crew member's date of birth
     * @throws CrewOperationException if age validation fails
     */
    public void validateAge(String name, LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw CrewOperationException.invalidAge(name, 0, MINIMUM_AGE, "Date of birth cannot be null");
        }
        
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw CrewOperationException.invalidAge(name, 0, MINIMUM_AGE, "Date of birth cannot be in the future");
        }
        
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        
        if (age < MINIMUM_AGE) {
            throw CrewOperationException.invalidAge(name, age, MINIMUM_AGE, 
                String.format("Crew member must be at least %d years old (maritime regulations)", MINIMUM_AGE));
        }
        
        if (age > MAXIMUM_AGE) {
            throw CrewOperationException.invalidAge(name, age, MAXIMUM_AGE, 
                String.format("Crew member cannot be older than %d years for active service", MAXIMUM_AGE));
        }
    }
    
    /**
     * Validates that the crew position is valid.
     * 
     * @param position The crew position to validate
     * @throws CrewOperationException if position is invalid
     */
    public void validatePosition(CrewPosition position) {
        if (position == null) {
            throw CrewOperationException.invalidRank("Position cannot be null");
        }
        // Note: CrewPosition enum provides type safety, so no additional validation needed
        // All enum values are valid by definition
    }
    
    /**
     * Validates certificate expiry date and warns about upcoming expiration.
     * 
     * Business Rules:
     * - Certificate expiry cannot be in the past
     * - Warning should be issued if certificate expires within 30 days
     * - Expired certificates prevent crew assignment to vessels
     * 
     * @param certificateExpiry The certificate expiry date
     * @throws CrewOperationException if certificate validation fails
     */
    public void validateCertificateExpiry(LocalDate certificateExpiry) {
        if (certificateExpiry == null) {
            return; // Certificate is optional
        }
        
        LocalDate today = LocalDate.now();
        
        if (certificateExpiry.isBefore(today)) {
            throw CrewOperationException.certificateExpired(null, certificateExpiry, 
                "Certificate has already expired");
        }
        
        // Check if certificate expires soon (warning, not error)
        LocalDate warningDate = today.plusDays(CERTIFICATE_WARNING_DAYS);
        if (certificateExpiry.isBefore(warningDate)) {
            // This could be logged as a warning in the calling service
            // For now, we'll allow it but the service can handle the warning
        }
    }
    
    /**
     * Validates passport number format.
     * 
     * Business Rules:
     * - Passport number must be 6-12 characters
     * - Can contain letters and numbers only
     * - Must be uppercase format
     * 
     * @param passportNumber The passport number to validate
     * @throws CrewOperationException if passport number is invalid
     */
    public void validatePassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.trim().isEmpty()) {
            throw CrewOperationException.invalidPassportNumber("Passport number cannot be null or empty");
        }
        
        String cleanPassport = passportNumber.trim().toUpperCase();
        
        if (!PASSPORT_PATTERN.matcher(cleanPassport).matches()) {
            throw CrewOperationException.invalidPassportNumber(
                "Passport number must be 6-12 characters long and contain only letters and numbers"
            );
        }
    }
    
    /**
     * Validates crew member name.
     * 
     * Business Rules:
     * - Name cannot be empty or just whitespace
     * - Name must be between 2 and 100 characters
     * - Name should contain valid characters (letters, spaces, hyphens, apostrophes)
     * 
     * @param name The crew member's name
     * @throws CrewOperationException if name validation fails
     */
    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw CrewOperationException.invalidCrewData("Name cannot be null or empty");
        }
        
        String trimmedName = name.trim();
        
        if (trimmedName.length() < 2) {
            throw CrewOperationException.invalidCrewData("Name must be at least 2 characters long");
        }
        
        if (trimmedName.length() > 100) {
            throw CrewOperationException.invalidCrewData("Name cannot exceed 100 characters");
        }
        
        // Check for valid name characters (letters, spaces, hyphens, apostrophes)
        if (!trimmedName.matches("^[a-zA-Z\\s\\-']+$")) {
            throw CrewOperationException.invalidCrewData("Name can only contain letters, spaces, hyphens, and apostrophes");
        }
    }
    
    /**
     * Validates nationality format.
     * 
     * @param nationality The crew member's nationality
     * @throws CrewOperationException if nationality validation fails
     */
    public void validateNationality(String nationality) {
        if (nationality == null || nationality.trim().isEmpty()) {
            throw CrewOperationException.invalidCrewData("Nationality cannot be null or empty");
        }
        
        if (nationality.length() > 50) {
            throw CrewOperationException.invalidCrewData("Nationality cannot exceed 50 characters");
        }
        
        // Basic validation - should contain only letters and spaces
        if (!nationality.trim().matches("^[a-zA-Z\\s]+$")) {
            throw CrewOperationException.invalidCrewData("Nationality can only contain letters and spaces");
        }
    }
    
    /**
     * Validates certificate name/type.
     * 
     * @param certificate The certificate name/type
     * @throws CrewOperationException if certificate validation fails
     */
    public void validateCertificate(String certificate) {
        if (certificate != null && !certificate.trim().isEmpty()) {
            if (certificate.length() > 50) {
                throw CrewOperationException.invalidCrewData("Certificate name cannot exceed 50 characters");
            }
        }
    }
    
    /**
     * Validates crew assignment to vessel based on certificates and qualifications.
     * 
     * Business Rules:
     * - Crew with expired certificates cannot be assigned to vessels
     * - Certain positions require valid certificates
     * - Captain and officers must have current certifications
     * 
     * @param position The crew member's position
     * @param certificateExpiry The certificate expiry date
     * @throws CrewOperationException if assignment validation fails
     */
    public void validateVesselAssignment(CrewPosition position, LocalDate certificateExpiry) {
        if (position == null) {
            throw CrewOperationException.invalidCrewData("Position is required for vessel assignment");
        }
        
        // Officers and Captain require valid certificates
        List<CrewPosition> requiresCertification = List.of(
            CrewPosition.CAPTAIN, 
            CrewPosition.DECK_OFFICER, 
            CrewPosition.ENGINE_OFFICER,
            CrewPosition.ENGINEER
        );
        
        if (requiresCertification.contains(position)) {
            if (certificateExpiry == null) {
                throw CrewOperationException.certificateExpired(null, null, 
                    String.format("Position %s requires a valid certificate", position.name()));
            }
            
            if (certificateExpiry.isBefore(LocalDate.now())) {
                throw CrewOperationException.certificateExpired(null, certificateExpiry, 
                    String.format("Cannot assign crew member with position %s - certificate has expired", position.name()));
            }
        }
    }
    
    /**
     * Checks if a certificate is expiring soon.
     * 
     * @param certificateExpiry The certificate expiry date
     * @return true if certificate expires within the warning period
     */
    public boolean isCertificateExpiringSoon(LocalDate certificateExpiry) {
        if (certificateExpiry == null) {
            return false;
        }
        
        LocalDate warningDate = LocalDate.now().plusDays(CERTIFICATE_WARNING_DAYS);
        return certificateExpiry.isBefore(warningDate) && !certificateExpiry.isBefore(LocalDate.now());
    }
    
    /**
     * Gets the list of valid crew positions from the enum.
     * 
     * @return Array of CrewPosition enum values
     */
    public CrewPosition[] getValidPositions() {
        return CrewPosition.values();
    }
    
    /**
     * Gets the minimum age requirement.
     * 
     * @return Minimum age in years
     */
    public int getMinimumAge() {
        return MINIMUM_AGE;
    }
    
    /**
     * Gets the maximum age for active service.
     * 
     * @return Maximum age in years
     */
    public int getMaximumAge() {
        return MAXIMUM_AGE;
    }
    
    /**
     * Gets the certificate warning period in days.
     * 
     * @return Warning period in days
     */
    public int getCertificateWarningDays() {
        return CERTIFICATE_WARNING_DAYS;
    }
}
