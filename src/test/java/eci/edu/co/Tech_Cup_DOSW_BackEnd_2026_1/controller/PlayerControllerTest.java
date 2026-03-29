package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        private PhotoUploadRequest photoUploadRequest;
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
                                .gender("Masculino")
                                .age(20)
                                .build();

                photoUploadRequest = PhotoUploadRequest.builder()
                                .photoUrl("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAn/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8VAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAA8A/9k=")
                                .build();

                availabilityRequest = AvailabilityRequest.builder()
                                .available(false)
                                .build();

                profileResponse = ProfileResponse.builder()
                                .id(1L)
                                .userId(1L)
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
                when(playerService.updateProfile(anyLong(), any(ProfileRequest.class)))
                                .thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(put("/api/players/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.userId").value(1))
                                .andExpect(jsonPath("$.jerseyNumber").value(10));
        }

        @Test
        @DisplayName("Should return 404 when profile not found")
        void testUpdateProfileNotFound() throws Exception {
                // Arrange
                when(playerService.updateProfile(anyLong(), any(ProfileRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Profile not found"));

                // Act & Assert
                mockMvc.perform(put("/api/players/999/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should change player availability successfully")
        void testChangeAvailabilitySuccess() throws Exception {
                // Arrange
                profileResponse.setAvailable(false);
                when(playerService.changeAvailability(anyLong(), any(AvailabilityRequest.class)))
                                .thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(patch("/api/players/1/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Should return 404 when changing availability for non-existent profile")
        void testChangeAvailabilityNotFound() throws Exception {
                // Arrange
                when(playerService.changeAvailability(anyLong(), any(AvailabilityRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Profile not found"));

                // Act & Assert
                mockMvc.perform(patch("/api/players/999/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should upload player photo successfully")
        void testUploadPhotoSuccess() throws Exception {
                // Arrange
                profileResponse.setPhotoUrl("http://example.com/new-photo.jpg");
                when(playerService.uploadPhoto(anyLong(), any(PhotoUploadRequest.class)))
                                .thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(post("/api/players/1/photo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(photoUploadRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Should return 404 when uploading photo for non-existent player")
        void testUploadPhotoNotFound() throws Exception {
                // Arrange
                when(playerService.uploadPhoto(anyLong(), any(PhotoUploadRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Player not found"));

                // Act & Assert
                mockMvc.perform(post("/api/players/999/photo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(photoUploadRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should get player profile successfully")
        void testGetProfileSuccess() throws Exception {
                // Arrange
                when(playerService.getProfile(anyLong())).thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(get("/api/players/1/profile"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.jerseyNumber").value(10));
        }

        @Test
        @DisplayName("Should return 404 when getting profile for non-existent player")
        void testGetProfileNotFound() throws Exception {
                // Arrange
                when(playerService.getProfile(anyLong()))
                                .thenThrow(new ResourceNotFoundException("Profile not found"));

                // Act & Assert
                mockMvc.perform(get("/api/players/999/profile"))
                                .andExpect(status().isNotFound());
        }
}
