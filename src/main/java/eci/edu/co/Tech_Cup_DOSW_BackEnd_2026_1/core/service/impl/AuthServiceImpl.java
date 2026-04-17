package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.UserMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.PasswordResetToken;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.VerificationToken;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.LoginRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.RegisterRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.AllowedIdentificationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.VerificationTokenEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.RevokedRefreshTokenEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.AllowedIdentificationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.PasswordResetTokenRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.RevokedRefreshTokenRepository;
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
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AuthServiceImpl implements AuthService {

    private static final Set<Role> PRIVILEGED_ROLES = EnumSet.of(Role.ADMINISTRATOR, Role.REFEREE, Role.ORGANIZER);

    private final UserRepository userRepository;
    private final AllowedIdentificationRepository allowedIdentificationRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RevokedRefreshTokenRepository revokedRefreshTokenRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RegisterRequestValidator registerRequestValidator;
    private final LoginRequestValidator loginRequestValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    @SuppressWarnings("null")
    public UserResponse register(RegisterRequest request) {
        registerRequestValidator.validate(request);
        log.info("Registering new user with email: {}", request.getEmail());
        log.debug("User registration details - name: {} {}, username: {}, role: {}",
                request.getFirstName(), request.getLastName(), request.getUsername(), request.getRole());

        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration failed: email {} already exists", request.getEmail());
            throw new ValidationException("Conflicto de unicidad en el registro",
                    java.util.Map.of("email", "El correo electrónico ya está registrado"));
        }

        Optional<UserEntity> existingUsername = userRepository.findByUsername(request.getUsername());
        if (existingUsername.isPresent()) {
            log.warn("Registration failed: username {} already exists", request.getUsername());
            throw new ValidationException("Conflicto de unicidad en el registro",
                    java.util.Map.of("username", "El nombre de usuario ya está en uso"));
        }
        log.debug("Email validation passed for: {}", request.getEmail());

        // Verificar cédula en lista blanca para roles privilegiados
        AllowedIdentificationEntity allowedEntry = null;
        if (PRIVILEGED_ROLES.contains(request.getRole())) {
            String identification = request.getIdentification().trim();
            allowedEntry = allowedIdentificationRepository
                    .findByIdentificationAndAllowedRoleAndUsedFalse(identification, request.getRole())
                    .orElseThrow(() -> {
                        log.warn("Registration blocked: identification {} not authorized for role {}",
                                identification, request.getRole());
                        return new ValidationException("Cédula no autorizada",
                                java.util.Map.of("identification",
                                        "La cédula no está registrada para el rol " + request.getRole().name()
                                        + " o ya fue utilizada"));
                    });
        }

        User user = mapToUser(request);
        log.debug("User entity created - id: {}, role: {}", user.getId(), user.getRole());

        UserEntity savedEntity = userRepository.save(userPersistenceMapper.toEntity(user));
        User savedUser = userPersistenceMapper.toModel(savedEntity);
        log.info("User registered successfully with id: {}", savedUser.getId());
        log.debug("User persisted to database with created timestamp: {}", savedUser.getCreatedAt());

        // Marcar cédula como utilizada (solo para roles privilegiados)
        if (allowedEntry != null) {
            allowedEntry.setUsed(true);
            allowedIdentificationRepository.save(allowedEntry);
            log.debug("Allowed identification marked as used for user: {}", savedUser.getId());
        }

        // Generar token de verificación
        String verificationToken = UUID.randomUUID().toString();
        VerificationToken token = VerificationToken.builder()
                .token(verificationToken)
                .userId(savedUser.getId())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24)) // Válido por 24 horas
                .verified(false)
                .build();
        verificationTokenRepository.save(userPersistenceMapper.toEntity(token));
        log.debug("Verification token created for user: {}", savedUser.getId());

        // Enviar email de verificación
        try {
            log.debug("Sending verification email to: {}", savedUser.getEmail());
            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    verificationToken,
                    savedUser.getUserType(),
                    savedUser.getRole());
        } catch (Exception e) {
            log.warn("Error sending verification email to {}: {}", savedUser.getEmail(), e.getMessage());
            // No lanzar excepción, el usuario se registró correctamente
        }

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        loginRequestValidator.validate(request);
        String identifier = request.getEmail() != null && !request.getEmail().isBlank()
                ? request.getEmail()
                : request.getUsername();
        log.info("Login attempt for identifier: {}", identifier);
        log.debug("Login request validation initiated");

        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier))
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Login failed: user with identifier {} not found", identifier);
                    return new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND);
                });
        log.debug("User found in database with id: {}, role: {}", user.getId(), user.getRole());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid password for identifier {}", identifier);
            throw new BusinessRuleException("Invalid password");
        }
        log.debug("Password validation passed for user: {}", user.getId());

        if (!user.isActive()) {
            log.warn("Login failed: user {} is inactive", identifier);
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
                .map(userPersistenceMapper::toModel)
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
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Email verification failed: user not found");
                    return new ResourceNotFoundException("User not found");
                });

        user.setActive(true);
        User updatedUser = userPersistenceMapper.toModel(
                userRepository.save(userPersistenceMapper.toEntity(user)));
        log.debug("User activated: {}", updatedUser.getId());

        // Marcar token como verificado
        verificationToken.setVerified(true);
        verificationToken.setVerifiedAt(LocalDateTime.now());
        verificationTokenRepository.save(userPersistenceMapper.toEntity(verificationToken));
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
    @SuppressWarnings("null")
    public String resendVerification(String email) {
        log.info("Resending verification email for: {}", email);

        User user = userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel)
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
        Optional<VerificationTokenEntity> existingToken = verificationTokenRepository
                .findByUserIdAndVerifiedFalse(user.getId());
        existingToken.ifPresent(tokenEntity -> {
            tokenEntity.setVerified(true); // Marcar como usado para invalidarlo
            verificationTokenRepository.save(tokenEntity);
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
        verificationTokenRepository.save(userPersistenceMapper.toEntity(verificationToken));
        log.debug("New verification token created for user: {}", user.getId());

        // Enviar email
        try {
            log.debug("Sending verification email to: {}", user.getEmail());
            emailService.sendVerificationEmailResend(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    newToken,
                    user.getUserType(),
                    user.getRole());
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

        if (revokedRefreshTokenRepository.existsByToken(refreshToken)) {
            log.warn("Refresh token failed: token was revoked");
            throw new BusinessRuleException("Invalid refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel)
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

    @Override
    public String logout(String refreshToken) {
        log.info("Logout request received");

        if (refreshToken == null || !jwtService.isTokenValid(refreshToken)
                || !jwtService.isRefreshToken(refreshToken)) {
            log.warn("Logout failed: invalid refresh token");
            throw new BusinessRuleException("Invalid refresh token");
        }

        if (!revokedRefreshTokenRepository.existsByToken(refreshToken)) {
            revokedRefreshTokenRepository.save(RevokedRefreshTokenEntity.builder()
                    .token(refreshToken)
                    .revokedAt(LocalDateTime.now())
                    .build());
        }

        return "Sesion cerrada correctamente";
    }

    @Override
    @SuppressWarnings("null")
    public String forgotPassword(String email) {
        log.info("Forgot password request for email: {}", email);

        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(email);
        if (userEntityOpt.isEmpty()) {
            log.info("Forgot password requested for non-existing email: {}", email);
            return "Si el correo existe, te enviaremos un enlace de recuperacion.";
        }

        User user = userPersistenceMapper.toModel(userEntityOpt.get());

        passwordResetTokenRepository.findByUserIdAndUsedFalse(user.getId()).ifPresent(existingToken -> {
            existingToken.setUsed(true);
            existingToken.setUsedAt(LocalDateTime.now());
            passwordResetTokenRepository.save(existingToken);
        });

        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken token = PasswordResetToken.builder()
                .token(resetToken)
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();
        passwordResetTokenRepository.save(userPersistenceMapper.toEntity(token));

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), user.getLastName(), resetToken);
        } catch (Exception e) {
            log.warn("Error sending password reset email to {}: {}", user.getEmail(), e.getMessage());
        }

        return "Si el correo existe, te enviaremos un enlace de recuperacion.";
    }

    @Override
    @SuppressWarnings("null")
    public String resetPassword(String token, String newPassword, String confirmPassword) {
        log.info("Reset password attempt with token");

        if (newPassword == null || newPassword.length() < 8) {
            throw new BusinessRuleException("La nueva contrasena debe tener minimo 8 caracteres");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessRuleException("La confirmacion de contrasena no coincide");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        if (resetToken.isUsed()) {
            throw new BusinessRuleException("Este token de recuperacion ya fue utilizado");
        }

        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new BusinessRuleException("El token de recuperacion expiro");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userPersistenceMapper.toEntity(user));

        resetToken.setUsed(true);
        resetToken.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(userPersistenceMapper.toEntity(resetToken));

        return "Contrasena restablecida exitosamente";
    }

    private User mapToUser(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(false);
        user.setCreatedAt(LocalDateTime.now());

        // Asegurar que userType está correctamente asignado
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }

        // Establecer identificación solo para roles privilegiados; limpiarla para el resto
        if (PRIVILEGED_ROLES.contains(request.getRole()) && request.getIdentification() != null) {
            user.setIdentification(request.getIdentification().trim());
        } else {
            user.setIdentification(null);
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
