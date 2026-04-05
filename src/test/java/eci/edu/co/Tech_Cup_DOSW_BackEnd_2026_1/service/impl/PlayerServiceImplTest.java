package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.PlayerServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.PhotoValidationUtil;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
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
                .firstName("Player")
                .lastName("One")
                .username("playerone")
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

    @Test
    @DisplayName("Should get profile successfully")
    void testGetProfileSuccess() {
        // Arrange
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Player One").position(Position.FORWARD).jerseyNumber(10).build();
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        // Act
        ProfileResponse response = playerService.getProfile(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Player One", response.getPlayerName());
        verify(sportProfileRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should search available players successfully without filters")
    void testSearchAvailablePlayersSuccess() {
        // Arrange
        SportProfile profile1 = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true)
                .semester(1).age(20).gender("Masculino").build();
        SportProfile profile2 = SportProfile.builder()
                .id(2L).userId(2L).position(Position.DEFENDER).available(true)
                .semester(2).age(21).gender("Masculino").build();

        Page<SportProfile> page = new PageImpl<>(Arrays.asList(profile1, profile2), PageRequest.of(0, 10), 2);

        ProfileResponse response1 = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Player One").position(Position.FORWARD).available(true).build();
        ProfileResponse response2 = ProfileResponse.builder()
                .id(2L).userId(2L).playerName("Player Two").position(Position.DEFENDER).available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).firstName("Player").lastName("Two").username("playertwo").build()));
        when(sportProfileMapper.toResponse(profile1)).thenReturn(response1);
        when(sportProfileMapper.toResponse(profile2)).thenReturn(response2);

        // Act
        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPlayers().size());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
        assertFalse(result.isHasNextPage());
    }

    @Test
    @DisplayName("Should search available players filtered by position")
    void testSearchAvailablePlayersByPosition() {
        // Arrange
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfile> page = new PageImpl<>(Arrays.asList(profile), PageRequest.of(0, 10), 1);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(Position.FORWARD, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        // Act
        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                Position.FORWARD, null, null, null, null, PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getPlayers().size());
        assertEquals(Position.FORWARD, result.getPlayers().get(0).getPosition());
    }

    @Test
    @DisplayName("Should search available players filtered by name")
    void testSearchAvailablePlayersByName() {
        // Arrange
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfile> page = new PageImpl<>(Arrays.asList(profile), PageRequest.of(0, 10), 1);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Juan").available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, "Juan", PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        // Act
        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, "Juan", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getPlayers().size());
    }

    @Test
    @DisplayName("Should return empty results when no players match filters")
    void testSearchAvailablePlayersNoResults() {
        // Arrange
        Page<SportProfile> page = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);

        // Act
        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPlayers().size());
        assertEquals(0, result.getTotalElements());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void testSearchAvailablePlayersWithPagination() {
        // Arrange
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfile> page = new PageImpl<>(Arrays.asList(profile), PageRequest.of(1, 5), 15);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(1, 5)))
                .thenReturn(page);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        // Act
        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(1, 5));

        // Assert
        assertEquals(1, result.getCurrentPage());
        assertEquals(15, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertEquals(5, result.getPageSize());
        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertTrue(result.isHasNextPage());
    }
}
