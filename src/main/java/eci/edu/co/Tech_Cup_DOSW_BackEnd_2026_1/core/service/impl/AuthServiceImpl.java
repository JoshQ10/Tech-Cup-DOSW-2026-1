package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.UserMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.LoginRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.RegisterRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RegisterRequestValidator registerRequestValidator;
    private final LoginRequestValidator loginRequestValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        registerRequestValidator.validate(request);
        log.info("Registering new user with email: {}", request.getEmail());
        log.debug("User registration details - name: {}, role: {}", request.getName(), request.getRole());

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration failed: email {} already exists", request.getEmail());
            throw new BusinessRuleException("Email already registered");
        }
        log.debug("Email validation passed for: {}", request.getEmail());

        User user = mapToUser(request);
        log.debug("User entity created - id: {}, role: {}", user.getId(), user.getRole());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        log.debug("User persisted to database with created timestamp: {}", savedUser.getCreatedAt());

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        loginRequestValidator.validate(request);
        log.info("Login attempt for email: {}", request.getEmail());
        log.debug("Login request validation initiated");

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: user with email {} not found", request.getEmail());
                    return new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND);
                });
        log.debug("User found in database with id: {}, role: {}", user.getId(), user.getRole());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid password for email {}", request.getEmail());
            throw new BusinessRuleException("Invalid password");
        }
        log.debug("Password validation passed for user: {}", user.getId());

        if (!user.isActive()) {
            log.warn("Login failed: user {} is inactive", request.getEmail());
            throw new BusinessRuleException("User account is inactive");
        }
        log.debug("User active status verified: true");

        log.info("Login successful for user: {}", user.getId());

        log.debug("Generating JWT tokens for user: {}", user.getId());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponse userResponse = mapToUserResponse(user);
        return LoginResponse.builder()
                .token(accessToken)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }

    @Override
    public UserResponse verifyEmail(String token) {
        log.info("Verifying email using token: {}", token);

        User user = userRepository.findByEmail(token)
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
                    return new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND);
                });

        log.info("Verification email resent successfully for user id: {}", user.getId());
        return "Verification email resent successfully";
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Refreshing JWT token");

        if (refreshToken == null || !jwtService.isTokenValid(refreshToken)
                || !jwtService.isRefreshToken(refreshToken)) {
            log.warn("Refresh token failed: invalid refresh token");
            throw new BusinessRuleException("Invalid refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Refresh token failed: user {} not found", email);
                    return new ResourceNotFoundException("User not found");
                });

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        log.info("Token refreshed successfully for user id: {}", user.getId());
        return LoginResponse.builder()
                .token(newAccessToken)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    private User mapToUser(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private UserResponse mapToUserResponse(User user) {
        return userMapper.toResponse(user);
    }
}
