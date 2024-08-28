package com.example.JwtAuthentication.service.auth;

import com.example.JwtAuthentication.dto.SignupRequest;
import com.example.JwtAuthentication.dto.UserDto;
import com.example.JwtAuthentication.entity.User;
import com.example.JwtAuthentication.enums.UserRole;
import com.example.JwtAuthentication.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount() {
        Optional<User> optionalAdmin = userRepository.findByUserRole(UserRole.ADMIN);

        if(optionalAdmin.isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@test.com");
            admin.setUserRole(UserRole.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode(System.getenv("ADMIN_PASSWORD")));
            userRepository.save(admin);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exists!");
        }
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public UserDto signup(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.USER);
        return userRepository.save(user).getUserDto();
    }
}
