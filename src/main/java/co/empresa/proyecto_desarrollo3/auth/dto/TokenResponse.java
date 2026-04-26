package co.empresa.proyecto_desarrollo3.auth.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private String refresh_token;

    
}