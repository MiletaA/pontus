package com.pontus.cargo.exception;

import java.time.LocalDateTime;

/**
 * Standard error response format for all API endpoints.
 * 
 * This class provides a consistent structure for error responses across the entire cargo service,
 * ensuring clients receive predictable error information regardless of the endpoint or error type.
 * 
 * @author Pontus Team
 * @version 1.0
 * @since 1.0
 */
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    
    public ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    // Getters
    public int getStatus() { 
        return status; 
    }
    
    public String getError() { 
        return error; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    // Setters (for JSON serialization frameworks that might need them)
    public void setStatus(int status) {
        this.status = status;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
