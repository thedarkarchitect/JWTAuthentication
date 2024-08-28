package com.example.JwtAuthentication.service.auth;

import com.example.JwtAuthentication.dto.SignupRequest;
import com.example.JwtAuthentication.dto.UserDto;

public interface AuthService {
    Boolean hasUserWithEmail(String email);

    UserDto signup(SignupRequest signupRequest);
}
