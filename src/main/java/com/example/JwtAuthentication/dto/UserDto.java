package com.example.JwtAuthentication.dto;

import com.example.JwtAuthentication.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private UserRole userRole;
}
