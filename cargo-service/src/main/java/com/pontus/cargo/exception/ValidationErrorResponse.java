package com.pontus.cargo.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Specialized error response for validation failures.
 * 
 * Extends ErrorResponse to include detailed field-level validation error information,
 * providing clients with specific feedback about which fields failed validation and why.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> fieldErrors;
    
    public ValidationErrorResponse(int status, String error, String message, LocalDateTime timestamp, Map<String, String> fieldErrors) {
        super(status, error, message, timestamp);
        this.fieldErrors = fieldErrors;
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    
    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
    
    @Override
    public String toString() {
        return "ValidationErrorResponse{" +
                "fieldErrors=" + fieldErrors +
                ", status=" + getStatus() +
                ", error='" + getError() + '\'' +
                ", message='" + getMessage() + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
