package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration failed: email {} already exists", request.getEmail());
            throw new BusinessRuleException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: user with email {} not found", request.getEmail());
                    return new ResourceNotFoundException("User not found");
                });

        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Login failed: invalid password for email {}", request.getEmail());
            throw new BusinessRuleException("Invalid password");
        }

        if (!user.isActive()) {
            log.warn("Login failed: user {} is inactive", request.getEmail());
            throw new BusinessRuleException("User account is inactive");
        }

        log.info("Login successful for user: {}", user.getId());

        UserResponse userResponse = mapToUserResponse(user);
        return LoginResponse.builder()
                .token("temp-token-" + user.getId())
                .user(userResponse)
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
