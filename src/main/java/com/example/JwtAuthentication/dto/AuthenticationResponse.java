package com.example.JwtAuthentication.dto;

import com.example.JwtAuthentication.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwtAccessToken;

    private String jwtRefreshToken;

    private Long userId;

    private UserRole userRole;
}
