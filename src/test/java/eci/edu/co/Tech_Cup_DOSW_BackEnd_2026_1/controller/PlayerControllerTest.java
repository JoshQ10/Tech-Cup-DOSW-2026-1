package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PlayerController Tests")
class PlayerControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PlayerService playerService;

        @MockBean
        private JwtService jwtService;

        private ProfileRequest profileRequest;
        private AvailabilityRequest availabilityRequest;
        private ProfileResponse profileResponse;

        @BeforeEach
        void setUp() {
                profileRequest = ProfileRequest.builder()
                                .position(Position.FORWARD)
                                .jerseyNumber(10)
                                .photoUrl("http://example.com/photo.jpg")
                                .available(true)
                                .semester(1)
                                .gender("M")
                                .age(20)
                                .build();

                availabilityRequest = AvailabilityRequest.builder()
                                .available(false)
                                .build();

                profileResponse = ProfileResponse.builder()
                                .id("profile123")
                                .userId("user123")
                                .position(Position.FORWARD)
                                .jerseyNumber(10)
                                .photoUrl("http://example.com/photo.jpg")
                                .available(true)
                                .build();
        }

        @Test
        @DisplayName("Should update player profile successfully")
        void testUpdateProfileSuccess() throws Exception {
                // Arrange
                when(playerService.updateProfile(anyString(), any(ProfileRequest.class)))
                                .thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(put("/api/players/profile123/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("profile123"))
                                .andExpect(jsonPath("$.userId").value("user123"))
                                .andExpect(jsonPath("$.jerseyNumber").value(10));
        }

        @Test
        @DisplayName("Should return 404 when profile not found")
        void testUpdateProfileNotFound() throws Exception {
                // Arrange
                when(playerService.updateProfile(anyString(), any(ProfileRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Profile not found"));

                // Act & Assert
                mockMvc.perform(put("/api/players/profile999/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should change player availability successfully")
        void testChangeAvailabilitySuccess() throws Exception {
                // Arrange
                profileResponse.setAvailable(false);
                when(playerService.changeAvailability(anyString(), any(AvailabilityRequest.class)))
                                .thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(patch("/api/players/profile123/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("profile123"));
        }

        @Test
        @DisplayName("Should return 404 when changing availability for non-existent profile")
        void testChangeAvailabilityNotFound() throws Exception {
                // Arrange
                when(playerService.changeAvailability(anyString(), any(AvailabilityRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Profile not found"));

                // Act & Assert
                mockMvc.perform(patch("/api/players/profile999/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isNotFound());
        }
}
