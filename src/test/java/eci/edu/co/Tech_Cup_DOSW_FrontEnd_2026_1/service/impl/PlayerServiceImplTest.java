package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Position;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.SportProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerServiceImpl Tests")
class PlayerServiceImplTest {

    @Mock
    private SportProfileRepository sportProfileRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private ProfileRequest profileRequest;
    private AvailabilityRequest availabilityRequest;
    private SportProfile testProfile;

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

        testProfile = SportProfile.builder()
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
    void testUpdateProfileSuccess() {
        // Arrange
        when(sportProfileRepository.findById("profile123")).thenReturn(Optional.of(testProfile));
        when(sportProfileRepository.save(any(SportProfile.class))).thenReturn(testProfile);

        // Act
        ProfileResponse response = playerService.updateProfile("profile123", profileRequest);

        // Assert
        assertNotNull(response);
        assertEquals("profile123", response.getId());
        assertEquals("user123", response.getUserId());
        verify(sportProfileRepository, times(1)).findById("profile123");
        verify(sportProfileRepository, times(1)).save(any(SportProfile.class));
    }

    @Test
    @DisplayName("Should fail when updating non-existent profile")
    void testUpdateProfileNotFound() {
        // Arrange
        when(sportProfileRepository.findById("profile123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> playerService.updateProfile("profile123", profileRequest));
        verify(sportProfileRepository, times(1)).findById("profile123");
    }

    @Test
    @DisplayName("Should change player availability successfully")
    void testChangeAvailabilitySuccess() {
        // Arrange
        when(sportProfileRepository.findById("profile123")).thenReturn(Optional.of(testProfile));
        when(sportProfileRepository.save(any(SportProfile.class))).thenReturn(testProfile);

        // Act
        ProfileResponse response = playerService.changeAvailability("profile123", availabilityRequest);

        // Assert
        assertNotNull(response);
        assertEquals("profile123", response.getId());
        verify(sportProfileRepository, times(1)).findById("profile123");
        verify(sportProfileRepository, times(1)).save(any(SportProfile.class));
    }

    @Test
    @DisplayName("Should fail when changing availability for non-existent profile")
    void testChangeAvailabilityNotFound() {
        // Arrange
        when(sportProfileRepository.findById("profile123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> playerService.changeAvailability("profile123", availabilityRequest));
        verify(sportProfileRepository, times(1)).findById("profile123");
    }
}
