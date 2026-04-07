package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.UserMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.VerificationToken;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.LoginRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.RegisterRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.VerificationTokenRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.AuthService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RegisterRequestValidator registerRequestValidator;
    private final LoginRequestValidator loginRequestValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final GoogleOAuth2Service googleOAuth2Service;

    @Override
    public UserResponse register(RegisterRequest request) {
        registerRequestValidator.validate(request);
        log.info("Registering new user with email: {}", request.getEmail());
        log.debug("User registration details - name: {} {}, username: {}, role: {}",
                request.getFirstName(), request.getLastName(), request.getUsername(), request.getRole());

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

        // Generar token de verificación
        String verificationToken = UUID.randomUUID().toString();
        VerificationToken token = VerificationToken.builder()
                .token(verificationToken)
                .userId(savedUser.getId())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24)) // Válido por 24 horas
                .verified(false)
                .build();
        @SuppressWarnings("null")
        VerificationToken savedToken = verificationTokenRepository.save(token);
        log.debug("Verification token created for user: {}", savedUser.getId());

        // Enviar email de verificación
        try {
            log.debug("Sending verification email to: {}", savedUser.getEmail());
            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    verificationToken,
                    savedUser.getUserType());
        } catch (Exception e) {
            log.warn("Error sending verification email to {}: {}", savedUser.getEmail(), e.getMessage());
            // No lanzar excepción, el usuario se registró correctamente
        }

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

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Email verification failed: invalid or expired token");
                    return new ResourceNotFoundException("Invalid or expired verification token");
                });

        // Validar que el token no esté expirado
        if (LocalDateTime.now().isAfter(verificationToken.getExpiresAt())) {
            log.warn("Email verification failed: token expired for user id: {}", verificationToken.getUserId());
            throw new BusinessRuleException("Verification token has expired. Please request a new one.");
        }

        // Validar que no haya sido verificado ya
        if (verificationToken.isVerified()) {
            log.warn("Email verification failed: token already used for user id: {}", verificationToken.getUserId());
            throw new BusinessRuleException("This verification token has already been used");
        }

        // Obtener usuario y activarlo
        Long userId = verificationToken.getUserId();
        @SuppressWarnings("null")
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Email verification failed: user not found");
                    return new ResourceNotFoundException("User not found");
                });

        user.setActive(true);
        User updatedUser = userRepository.save(user);
        log.debug("User activated: {}", updatedUser.getId());

        // Marcar token como verificado
        verificationToken.setVerified(true);
        verificationToken.setVerifiedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);
        log.info("Email verified successfully for user id: {}", updatedUser.getId());

        // Enviar email de bienvenida
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName(), user.getLastName());
        } catch (Exception e) {
            log.warn("Error sending welcome email to {}: {}", user.getEmail(), e.getMessage());
        }

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

        // Validar que el usuario no esté ya verificado
        if (user.isActive()) {
            log.warn("Resend verification failed: user {} is already active", email);
            throw new BusinessRuleException("User account is already verified");
        }

        // Invalidar token antiguo si existe
        Optional<VerificationToken> existingToken = verificationTokenRepository
                .findByUserIdAndVerifiedFalse(user.getId());
        existingToken.ifPresent(token -> {
            token.setVerified(true); // Marcar como usado para invalidarlo
            verificationTokenRepository.save(token);
            log.debug("Previous verification token invalidated for user: {}", user.getId());
        });

        // Generar nuevo token
        String newToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(newToken)
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .verified(false)
                .build();
        @SuppressWarnings("null")
        VerificationToken savedNewToken = verificationTokenRepository.save(verificationToken);
        log.debug("New verification token created for user: {}", user.getId());

        // Enviar email
        try {
            log.debug("Sending verification email to: {}", user.getEmail());
            emailService.sendVerificationEmailResend(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    newToken,
                    user.getUserType());
            log.info("Verification email resent successfully for user: {}", user.getId());
            return "Verification email resent successfully. Please check your inbox.";
        } catch (Exception e) {
            log.warn("Error sending verification email to {}: {}", user.getEmail(), e.getMessage());
            return "Verification email resend failed. Please try again later.";
        }
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
        user.setActive(false); // Por defecto inactivo hasta verificar email
        user.setCreatedAt(LocalDateTime.now());

        // Asegurar que userType está correctamente asignado
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }

        return user;
    }

    private UserResponse mapToUserResponse(User user) {
        return userMapper.toResponse(user);
    }

    @Override
    public LoginResponse loginWithGoogle(String idToken) {
        log.info("Google login attempt");
        log.debug("Validating Google ID token");

        User user = googleOAuth2Service.validateAndGetGoogleUser(idToken);
        log.info("Google login successful for user: {}", user.getId());

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
}
