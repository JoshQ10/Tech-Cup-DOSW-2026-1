package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role(Role.PLAYER)
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        testUser = User.builder()
                .id("user123")
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role(Role.PLAYER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userResponse = UserResponse.builder()
                .id("user123")
                .name("Test User")
                .email("test@example.com")
                .role(Role.PLAYER)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(Role.PLAYER, response.getRole());
        assertTrue(response.isActive());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should fail when registering with existing email")
    void testRegisterUserWithExistingEmail() {
        // Arrange
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> authService.register(registerRequest));
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void testLoginSuccess() {
        // Arrange
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getUser());
        assertEquals("test@example.com", response.getUser().getEmail());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should fail login when user not found")
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should fail login with incorrect password")
    void testLoginWithIncorrectPassword() {
        // Arrange
        loginRequest.setPassword("wrongpassword");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should fail login when user is inactive")
    void testLoginWithInactiveUser() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }
}
