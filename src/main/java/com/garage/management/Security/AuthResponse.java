package com.garage.management.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private int status;
    private String message;
    private String token;

    public AuthResponse(int status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}