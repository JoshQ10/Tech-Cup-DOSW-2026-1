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

        User user = mapToUser(request);

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

    @Override
    public UserResponse verifyEmail(String token) {
        log.info("Verifying email using token: {}", token);

        User user = userRepository.findById(token)
                .orElseThrow(() -> {
                    log.warn("Email verification failed: user with token {} not found", token);
                    return new ResourceNotFoundException("User not found for verification token");
                });

        user.setActive(true);
        User updatedUser = userRepository.save(user);
        log.info("Email verified successfully for user id: {}", updatedUser.getId());

        return mapToUserResponse(updatedUser);
    }

    @Override
    public String resendVerification(String email) {
        log.info("Resending verification email for: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Resend verification failed: user with email {} not found", email);
                    return new ResourceNotFoundException("User not found");
                });

        log.info("Verification email resent successfully for user id: {}", user.getId());
        return "Verification email resent successfully";
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Refreshing JWT token");

        if (refreshToken == null || !refreshToken.startsWith("temp-token-")) {
            log.warn("Refresh token failed: invalid token format");
            throw new BusinessRuleException("Invalid refresh token");
        }

        String userId = refreshToken.substring("temp-token-".length());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Refresh token failed: user {} not found", userId);
                    return new ResourceNotFoundException("User not found");
                });

        log.info("Token refreshed successfully for user id: {}", user.getId());
        return LoginResponse.builder()
                .token("temp-token-" + user.getId() + "-refreshed")
                .user(mapToUserResponse(user))
                .build();
    }

    private User mapToUser(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
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
