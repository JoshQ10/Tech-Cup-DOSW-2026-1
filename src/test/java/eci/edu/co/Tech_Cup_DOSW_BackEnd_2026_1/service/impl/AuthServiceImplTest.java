package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.UserMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.VerificationToken;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.AuthServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.GoogleOAuth2Service;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.LoginRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.RegisterRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.VerificationTokenEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Tests")
@SuppressWarnings("null")
class AuthServiceImplTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private VerificationTokenRepository verificationTokenRepository;

        @Mock
        private JwtService jwtService;

        @Mock
        private UserMapper userMapper;

        @Mock
        private RegisterRequestValidator registerRequestValidator;

        @Mock
        private LoginRequestValidator loginRequestValidator;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private EmailService emailService;

        @Mock
        private GoogleOAuth2Service googleOAuth2Service;

        @Mock
        private UserPersistenceMapper userPersistenceMapper;

        @InjectMocks
        private AuthServiceImpl authService;

        private RegisterRequest registerRequest;
        private LoginRequest loginRequest;
        private User testUser;
        private UserEntity testUserEntity;

        @BeforeEach
        void setUp() {
                String institutionalEmail = "123.456-7@escuelaing.edu.co";

                registerRequest = RegisterRequest.builder()
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email(institutionalEmail)
                                .password("password123")
                                .confirmPassword("password123")
                                .userType(UserType.INTERNAL)
                                .role(Role.PLAYER)
                                .build();

                loginRequest = LoginRequest.builder()
                                .email(institutionalEmail)
                                .password("password123")
                                .build();

                testUser = User.builder()
                                .id(1L)
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email(institutionalEmail)
                                .password("encoded-password")
                                .role(Role.PLAYER)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();

                testUserEntity = UserEntity.builder()
                                .id(1L)
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email(institutionalEmail)
                                .password("encoded-password")
                                .role(Role.PLAYER)
                                .active(true)
                                .build();
        }

        @Test
        @DisplayName("Should register a new user successfully")
        void testRegisterUserSuccess() {
                UserResponse expectedResponse = UserResponse.builder()
                                .id(1L).firstName("Test").lastName("User").username("testuser")
                                .email("test@example.com")
                                .role(Role.PLAYER).active(false).build();

                doNothing().when(registerRequestValidator).validate(any(RegisterRequest.class));
                when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
                when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
                when(userMapper.toEntity(any(RegisterRequest.class))).thenAnswer(inv -> {
                        RegisterRequest r = inv.getArgument(0);
                        return User.builder()
                                        .firstName(r.getFirstName())
                                        .lastName(r.getLastName())
                                        .username(r.getUsername())
                                        .email(r.getEmail())
                                        .password("")
                                        .role(r.getRole())
                                        .userType(r.getUserType())
                                        .build();
                });
                when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
                when(userPersistenceMapper.toEntity(any(User.class))).thenReturn(UserEntity.builder().build());
                when(userRepository.save(any(UserEntity.class))).thenReturn(testUserEntity);
                when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(
                                User.builder()
                                                .id(1L)
                                                .firstName("Test")
                                                .lastName("User")
                                                .username("testuser")
                                                .email("test@example.com")
                                                .password("encoded-password")
                                                .role(Role.PLAYER)
                                                .active(false)
                                                .createdAt(LocalDateTime.now())
                                                .build());
                when(userPersistenceMapper.toEntity(any(VerificationToken.class)))
                                .thenReturn(VerificationTokenEntity.builder().build());
                when(verificationTokenRepository.save(any(VerificationTokenEntity.class)))
                                .thenAnswer(inv -> inv.getArgument(0));
                when(userMapper.toResponse(any(User.class))).thenReturn(expectedResponse);

                UserResponse response = authService.register(registerRequest);

                assertNotNull(response);
                assertEquals("Test", response.getFirstName());
                assertEquals("User", response.getLastName());
                assertEquals("test@example.com", response.getEmail());
                assertEquals("testuser", response.getUsername());
                assertEquals(Role.PLAYER, response.getRole());
                assertFalse(response.isActive());
                verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
                verify(userRepository, times(1)).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("Should fail when registering with existing email")
        void testRegisterUserWithExistingEmail() {
                doNothing().when(registerRequestValidator).validate(any(RegisterRequest.class));
                when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(testUserEntity));

                assertThrows(ValidationException.class, () -> authService.register(registerRequest));
                verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
                verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should login successfully with correct credentials")
        void testLoginSuccess() {
                UserResponse expectedResponse = UserResponse.builder()
                                .id(1L).firstName("Test").lastName("User").username("testuser")
                                .email("123.456-7@escuelaing.edu.co")
                                .role(Role.PLAYER).active(true).build();

                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUserEntity));
                when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
                when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);
                when(jwtService.generateAccessToken(testUser)).thenReturn("access-token");
                when(jwtService.generateRefreshToken(testUser)).thenReturn("refresh-token");
                when(userMapper.toResponse(any(User.class))).thenReturn(expectedResponse);

                LoginResponse response = authService.login(loginRequest);

                assertNotNull(response);
                assertNotNull(response.getToken());
                assertNotNull(response.getUser());
                assertEquals("123.456-7@escuelaing.edu.co", response.getUser().getEmail());
                assertEquals("access-token", response.getAccessToken());
                assertEquals("refresh-token", response.getRefreshToken());
                verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        }

        @Test
        @DisplayName("Should login PLAYER with non-institutional email")
        void testLoginPlayerWithNonInstitutionalEmail() {
                User externalPlayer = User.builder()
                                .id(2L)
                                .firstName("External")
                                .lastName("Player")
                                .username("extplayer")
                                .email("player@gmail.com")
                                .password("encoded-password")
                                .role(Role.PLAYER)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();

                UserEntity externalPlayerEntity = UserEntity.builder()
                                .id(2L)
                                .firstName("External")
                                .lastName("Player")
                                .username("extplayer")
                                .email("player@gmail.com")
                                .password("encoded-password")
                                .role(Role.PLAYER)
                                .active(true)
                                .build();

                UserResponse expectedResponse = UserResponse.builder()
                                .id(2L)
                                .firstName("External")
                                .lastName("Player")
                                .username("extplayer")
                                .email("player@gmail.com")
                                .role(Role.PLAYER)
                                .active(true)
                                .build();

                LoginRequest externalLogin = LoginRequest.builder()
                                .email("player@gmail.com")
                                .password("password123")
                                .build();

                when(userRepository.findByEmail("player@gmail.com")).thenReturn(Optional.of(externalPlayerEntity));
                when(userPersistenceMapper.toModel(externalPlayerEntity)).thenReturn(externalPlayer);
                when(passwordEncoder.matches("password123", externalPlayer.getPassword())).thenReturn(true);
                when(jwtService.generateAccessToken(externalPlayer)).thenReturn("access-token");
                when(jwtService.generateRefreshToken(externalPlayer)).thenReturn("refresh-token");
                when(userMapper.toResponse(any(User.class))).thenReturn(expectedResponse);

                LoginResponse response = authService.login(externalLogin);

                assertNotNull(response);
                assertEquals("access-token", response.getAccessToken());
                assertEquals("refresh-token", response.getRefreshToken());
                assertEquals("player@gmail.com", response.getUser().getEmail());
                verify(userRepository, times(1)).findByEmail("player@gmail.com");
        }

        @Test
        @DisplayName("Should fail login when user not found")
        void testLoginUserNotFound() {
                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
                when(userRepository.findByUsername(loginRequest.getEmail())).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));
                verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        }

        @Test
        @DisplayName("Should fail login with incorrect password")
        void testLoginWithIncorrectPassword() {
                loginRequest.setPassword("wrongpassword");
                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUserEntity));
                when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);

                assertThrows(BusinessRuleException.class, () -> authService.login(loginRequest));
                verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        }

        @Test
        @DisplayName("Should fail login when user is inactive")
        void testLoginWithInactiveUser() {
                testUser.setActive(false);
                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUserEntity));
                when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);

                assertThrows(BusinessRuleException.class, () -> authService.login(loginRequest));
                verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        }
}
