package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
@SuppressWarnings("null")
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private AuthService authService;

        @MockBean
        private JwtService jwtService;

        private RegisterRequest registerRequest;
        private LoginRequest loginRequest;
        private UserResponse userResponse;
        private LoginResponse loginResponse;

        @BeforeEach
        void setUp() {
                registerRequest = RegisterRequest.builder()
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email("test@example.com")
                                .password("password123")
                                .confirmPassword("password123")
                                .userType(UserType.INTERNAL)
                                .role(Role.PLAYER)
                                .build();

                loginRequest = LoginRequest.builder()
                                .email("test@example.com")
                                .password("password123")
                                .build();

                userResponse = UserResponse.builder()
                                .id(1L)
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email("test@example.com")
                                .userType(UserType.INTERNAL)
                                .role(Role.PLAYER)
                                .active(true)
                                .build();

                loginResponse = LoginResponse.builder()
                                .token("temp-token-user123")
                                .user(userResponse)
                                .build();
        }

        @Test
        @DisplayName("Should register user successfully")
        void testRegisterSuccess() throws Exception {
                // Arrange
                when(authService.register(any(RegisterRequest.class))).thenReturn(userResponse);

                // Act & Assert
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.email").value("test@example.com"))
                                .andExpect(jsonPath("$.name").value("Test User"));
        }

        @Test
        @DisplayName("Should return 400 when registering with existing email")
        void testRegisterWithExistingEmail() throws Exception {
                // Arrange
                when(authService.register(any(RegisterRequest.class)))
                                .thenThrow(new BusinessRuleException("Email already registered"));

                // Act & Assert
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should login successfully")
        void testLoginSuccess() throws Exception {
                // Arrange
                when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").isNotEmpty())
                                .andExpect(jsonPath("$.user.email").value("test@example.com"));
        }

        @Test
        @DisplayName("Should return 404 when logging in with non-existent user")
        void testLoginUserNotFound() throws Exception {
                // Arrange
                when(authService.login(any(LoginRequest.class)))
                                .thenThrow(new ResourceNotFoundException("User not found"));

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when logging in with invalid password")
        void testLoginWithInvalidPassword() throws Exception {
                // Arrange
                when(authService.login(any(LoginRequest.class)))
                                .thenThrow(new BusinessRuleException("Invalid password"));

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isBadRequest());
        }
}
