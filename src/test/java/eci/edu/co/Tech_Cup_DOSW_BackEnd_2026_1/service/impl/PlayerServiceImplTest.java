package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.PlayerServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.PhotoValidationUtil;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.UserRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerServiceImpl Tests")
class PlayerServiceImplTest {

    @Mock
    private SportProfileRepository sportProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SportProfileMapper sportProfileMapper;

    @Mock
    private PhotoValidationUtil photoValidationUtil;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private ProfileRequest profileRequest;
    private AvailabilityRequest availabilityRequest;
    private SportProfile testProfile;
    private User testUser;

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

        availabilityRequest = AvailabilityRequest.builder()
                .available(false)
                .build();

        testProfile = SportProfile.builder()
                .id(1L)
                .userId(1L)
                .position(Position.FORWARD)
                .jerseyNumber(10)
                .photoUrl("http://example.com/photo.jpg")
                .available(true)
                .build();

        testUser = User.builder()
                .id(1L)
                .name("Player One")
                .build();
    }

    @Test
    @DisplayName("Should update player profile successfully")
    void testUpdateProfileSuccess() {
        // Arrange
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).jerseyNumber(10).available(true).build();
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(sportProfileRepository.save(any(SportProfile.class))).thenReturn(testProfile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        // Act
        ProfileResponse response = playerService.updateProfile(1L, profileRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        verify(sportProfileRepository, times(1)).findById(1L);
        verify(sportProfileRepository, times(1)).save(any(SportProfile.class));
    }

    @Test
    @DisplayName("Should fail when updating non-existent profile")
    void testUpdateProfileNotFound() {
        // Arrange
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> playerService.updateProfile(1L, profileRequest));
        verify(sportProfileRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should change player availability successfully")
    void testChangeAvailabilitySuccess() {
        // Arrange
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).jerseyNumber(10).available(false).build();
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(sportProfileRepository.save(any(SportProfile.class))).thenReturn(testProfile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        // Act
        ProfileResponse response = playerService.changeAvailability(1L, availabilityRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(sportProfileRepository, times(1)).findById(1L);
        verify(sportProfileRepository, times(1)).save(any(SportProfile.class));
    }

    @Test
    @DisplayName("Should fail when changing availability for non-existent profile")
    void testChangeAvailabilityNotFound() {
        // Arrange
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> playerService.changeAvailability(1L, availabilityRequest));
        verify(sportProfileRepository, times(1)).findById(1L);
    }
}
