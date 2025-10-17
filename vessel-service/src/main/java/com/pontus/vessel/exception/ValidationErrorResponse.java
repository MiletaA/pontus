package com.pontus.vessel.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extended error response for validation failures that includes field-specific error details.
 * 
 * This class extends the standard ErrorResponse to provide detailed information about
 * validation failures, including which specific fields failed validation and why.
 * Primarily used for handling @Valid annotation failures and request body validation.
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
                "status=" + getStatus() +
                ", error='" + getError() + '\'' +
                ", message='" + getMessage() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", fieldErrors=" + fieldErrors +
                '}';
    }
}
