package com.example.JwtAuthentication.controller;

import com.example.JwtAuthentication.dto.AuthenticationRequest;
import com.example.JwtAuthentication.dto.AuthenticationResponse;
import com.example.JwtAuthentication.dto.SignupRequest;
import com.example.JwtAuthentication.dto.UserDto;
import com.example.JwtAuthentication.entity.User;
import com.example.JwtAuthentication.repository.UserRepository;
import com.example.JwtAuthentication.service.auth.AuthService;
import com.example.JwtAuthentication.service.jwt.UserService;
import com.example.JwtAuthentication.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if(authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists");
        }

        UserDto userDto = authService.signup(signupRequest);
        if(userDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

        String accessToken = jwtUtil.generateAccessToken(authenticationRequest.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(authenticationRequest.getEmail());

        AuthenticationResponse response = new AuthenticationResponse();

        if(optionalUser.isPresent()){
            response.setJwtRefreshToken(refreshToken);
            response.setJwtAccessToken(accessToken);
            response.setUserRole(optionalUser.get().getUserRole());
            response.setUserId(optionalUser.get().getId());

        }

        return response;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        if(jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh token is expired");
        }

        String email = jwtUtil.extractUsername(refreshToken);

        Optional<User> optionalUser = userRepository.findFirstByEmail(email);
        String accessToken = jwtUtil.generateAccessToken(email);

        AuthenticationResponse response = new AuthenticationResponse();

        if(optionalUser.isPresent()){
            response.setJwtRefreshToken(refreshToken);
            response.setJwtAccessToken(accessToken);
            response.setUserRole(optionalUser.get().getUserRole());
            response.setUserId(optionalUser.get().getId());

        }
        return ResponseEntity.ok(response);
    }
}
