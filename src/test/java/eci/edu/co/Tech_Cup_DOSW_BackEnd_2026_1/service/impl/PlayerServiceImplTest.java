package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.SportsProfileUpdateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.FileStorageService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.PlayerServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.PhotoValidationUtil;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.SportProfileEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
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
import org.springframework.web.multipart.MultipartFile;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private ProfileRequest profileRequest;
    private AvailabilityRequest availabilityRequest;
    private SportProfile testProfile;
    private SportProfileEntity testProfileEntity;
    private User testUser;
    private UserEntity testUserEntity;

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

        testProfileEntity = SportProfileEntity.builder()
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

        testUserEntity = UserEntity.builder()
                .id(1L)
                .firstName("Player")
                .lastName("One")
                .username("playerone")
                .build();
    }

    private void stubProfileFindAndMap() {
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.of(testProfileEntity));
        when(userPersistenceMapper.toModel(any(SportProfileEntity.class))).thenReturn(testProfile);
    }

    private void stubProfilePersist() {
        when(userPersistenceMapper.toEntity(any(SportProfile.class))).thenReturn(testProfileEntity);
        when(sportProfileRepository.save(any(SportProfileEntity.class))).thenReturn(testProfileEntity);
    }

    private void stubUserLookup() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
        when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
    }

    @Test
    @DisplayName("Should update player profile successfully")
    void testUpdateProfileSuccess() {
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).jerseyNumber(10).available(true).build();
        stubProfileFindAndMap();
        doNothing().when(photoValidationUtil).validateBase64Photo(any());
        stubProfilePersist();
        stubUserLookup();
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.updateProfile(1L, profileRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        verify(sportProfileRepository, times(1)).findById(1L);
        verify(sportProfileRepository, times(1)).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should fail when updating non-existent profile")
    void testUpdateProfileNotFound() {
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.updateProfile(1L, profileRequest));
        verify(sportProfileRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should change player availability successfully")
    void testChangeAvailabilitySuccess() {
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).jerseyNumber(10).available(false).build();
        stubProfileFindAndMap();
        stubProfilePersist();
        stubUserLookup();
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.changeAvailability(1L, availabilityRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(sportProfileRepository, times(1)).findById(1L);
        verify(sportProfileRepository, times(1)).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should fail when changing availability for non-existent profile")
    void testChangeAvailabilityNotFound() {
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.changeAvailability(1L, availabilityRequest));
        verify(sportProfileRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should get profile successfully")
    void testGetProfileSuccess() {
        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Player One").position(Position.FORWARD).jerseyNumber(10).build();
        stubProfileFindAndMap();
        stubUserLookup();
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.getProfile(1L);

        assertNotNull(response);
        assertEquals("Player One", response.getPlayerName());
        verify(sportProfileRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should search available players successfully without filters")
    void testSearchAvailablePlayersSuccess() {
        SportProfile profile1 = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true)
                .semester(1).age(20).gender("Masculino").build();
        SportProfile profile2 = SportProfile.builder()
                .id(2L).userId(2L).position(Position.DEFENDER).available(true)
                .semester(2).age(21).gender("Masculino").build();

        SportProfileEntity entity1 = SportProfileEntity.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true)
                .semester(1).age(20).gender("Masculino").build();
        SportProfileEntity entity2 = SportProfileEntity.builder()
                .id(2L).userId(2L).position(Position.DEFENDER).available(true)
                .semester(2).age(21).gender("Masculino").build();

        Page<SportProfileEntity> page = new PageImpl<>(Arrays.asList(entity1, entity2), PageRequest.of(0, 10), 2);

        ProfileResponse response1 = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Player One").position(Position.FORWARD).available(true).build();
        ProfileResponse response2 = ProfileResponse.builder()
                .id(2L).userId(2L).playerName("Player Two").position(Position.DEFENDER).available(true).build();

        User user2 = User.builder().id(2L).firstName("Player").lastName("Two").username("playertwo").build();
        UserEntity userEntity2 = UserEntity.builder().id(2L).firstName("Player").lastName("Two").username("playertwo").build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(profile1);
        when(userPersistenceMapper.toModel(entity2)).thenReturn(profile2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity2));
        when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
        when(userPersistenceMapper.toModel(userEntity2)).thenReturn(user2);
        when(sportProfileMapper.toResponse(profile1)).thenReturn(response1);
        when(sportProfileMapper.toResponse(profile2)).thenReturn(response2);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(0, 10));

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
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        SportProfileEntity entity = SportProfileEntity.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfileEntity> page = new PageImpl<>(Arrays.asList(entity), PageRequest.of(0, 10), 1);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(Position.FORWARD, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userPersistenceMapper.toModel(entity)).thenReturn(profile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
        when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                Position.FORWARD, null, null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getPlayers().size());
        assertEquals(Position.FORWARD, result.getPlayers().get(0).getPosition());
    }

    @Test
    @DisplayName("Should search available players filtered by name")
    void testSearchAvailablePlayersByName() {
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        SportProfileEntity entity = SportProfileEntity.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfileEntity> page = new PageImpl<>(Arrays.asList(entity), PageRequest.of(0, 10), 1);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).playerName("Juan").available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, "Juan", PageRequest.of(0, 10)))
                .thenReturn(page);
        when(userPersistenceMapper.toModel(entity)).thenReturn(profile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
        when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, "Juan", PageRequest.of(0, 10));

        assertEquals(1, result.getPlayers().size());
    }

    @Test
    @DisplayName("Should return empty results when no players match filters")
    void testSearchAvailablePlayersNoResults() {
        Page<SportProfileEntity> page = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(0, 10)))
                .thenReturn(page);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(0, result.getPlayers().size());
        assertEquals(0, result.getTotalElements());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void testSearchAvailablePlayersWithPagination() {
        SportProfile profile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        SportProfileEntity entity = SportProfileEntity.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();
        Page<SportProfileEntity> page = new PageImpl<>(Arrays.asList(entity), PageRequest.of(1, 5), 15);

        ProfileResponse response = ProfileResponse.builder()
                .id(1L).userId(1L).available(true).build();

        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, PageRequest.of(1, 5)))
                .thenReturn(page);
        when(userPersistenceMapper.toModel(entity)).thenReturn(profile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUserEntity));
        when(userPersistenceMapper.toModel(testUserEntity)).thenReturn(testUser);
        when(sportProfileMapper.toResponse(profile)).thenReturn(response);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, PageRequest.of(1, 5));

        assertEquals(1, result.getCurrentPage());
        assertEquals(15, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertEquals(5, result.getPageSize());
        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertTrue(result.isHasNextPage());
    }

    @Test
    @DisplayName("Should upload photo successfully")
    void testUploadPhotoSuccess() {
        PhotoUploadRequest photoRequest = PhotoUploadRequest.builder()
                .photoUrl("data:image/jpeg;base64,/9j/4AAQSkZJRgABA...")
                .build();

        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();

        stubProfileFindAndMap();
        doNothing().when(photoValidationUtil).validateBase64Photo(anyString());
        stubProfilePersist();
        stubUserLookup();
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.uploadPhoto(1L, photoRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(photoValidationUtil).validateBase64Photo(photoRequest.getPhotoUrl());
        verify(sportProfileRepository).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should fail when uploading photo for non-existent profile")
    void testUploadPhotoNotFound() {
        PhotoUploadRequest photoRequest = PhotoUploadRequest.builder()
                .photoUrl("data:image/jpeg;base64,/9j/4AAQSkZJRgABA...")
                .build();

        doNothing().when(photoValidationUtil).validateBase64Photo(anyString());
        when(sportProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.uploadPhoto(1L, photoRequest));
        verify(sportProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when uploading invalid base64 photo")
    void testUploadPhotoInvalidBase64() {
        PhotoUploadRequest photoRequest = PhotoUploadRequest.builder()
                .photoUrl("invalid-base64")
                .build();

        doThrow(new IllegalArgumentException("Invalid base64 photo")).when(photoValidationUtil)
                .validateBase64Photo("invalid-base64");

        assertThrows(IllegalArgumentException.class, () -> playerService.uploadPhoto(1L, photoRequest));
        verify(sportProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should upsert sports profile by user id successfully - create new")
    void testUpsertSportsProfileByUserIdCreateSuccess() {
        SportsProfileUpdateRequest updateRequest = SportsProfileUpdateRequest.builder()
                .primaryPosition(Position.FORWARD)
                .secondaryPosition(Position.MIDFIELDER)
                .jerseyNumber(10)
                .dominantFoot(DominantFoot.RIGHT)
                .available(true)
                .build();

        SportProfile newProfile = SportProfile.builder()
                .userId(1L)
                .position(Position.FORWARD)
                .secondaryPosition(Position.MIDFIELDER)
                .jerseyNumber(10)
                .dominantFoot(DominantFoot.RIGHT)
                .available(true)
                .build();

        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).build();

        stubUserLookup();
        when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userPersistenceMapper.toEntity(any(SportProfile.class))).thenReturn(testProfileEntity);
        when(sportProfileRepository.save(any(SportProfileEntity.class))).thenReturn(testProfileEntity);
        when(userPersistenceMapper.toModel(testProfileEntity)).thenReturn(newProfile);
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.upsertSportsProfileByUserId(1L, updateRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(sportProfileRepository).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should upsert sports profile by user id successfully - update existing")
    void testUpsertSportsProfileByUserIdUpdateSuccess() {
        SportsProfileUpdateRequest updateRequest = SportsProfileUpdateRequest.builder()
                .primaryPosition(Position.DEFENDER)
                .secondaryPosition(null)
                .jerseyNumber(4)
                .dominantFoot(DominantFoot.LEFT)
                .available(false)
                .build();

        SportProfile existingProfile = SportProfile.builder()
                .id(1L).userId(1L).position(Position.DEFENDER).jerseyNumber(4).available(false).build();

        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.DEFENDER).available(false).build();

        stubUserLookup();
        when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfileEntity));
        when(userPersistenceMapper.toModel(testProfileEntity)).thenReturn(existingProfile);
        when(userPersistenceMapper.toEntity(any(SportProfile.class))).thenReturn(testProfileEntity);
        when(sportProfileRepository.save(any(SportProfileEntity.class))).thenReturn(testProfileEntity);
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.upsertSportsProfileByUserId(1L, updateRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(sportProfileRepository).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should fail upsert when user not found")
    void testUpsertSportsProfileByUserIdUserNotFound() {
        SportsProfileUpdateRequest updateRequest = SportsProfileUpdateRequest.builder()
                .primaryPosition(Position.FORWARD)
                .jerseyNumber(10)
                .available(true)
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.upsertSportsProfileByUserId(99L, updateRequest));
    }

    @Test
    @DisplayName("Should upload full photo by user id successfully")
    void testUploadFullPhotoByUserIdSuccess() {
        MultipartFile mockFile = mock(MultipartFile.class);
        String photoUrl = "https://storage.example.com/photo.jpg";

        SportProfile profileWithPhoto = SportProfile.builder()
                .id(1L).userId(1L).fullPhotoUrl(photoUrl).build();

        ProfileResponse expectedResponse = ProfileResponse.builder()
                .id(1L).userId(1L).fullPhotoUrl(photoUrl).build();

        stubUserLookup();
        when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userPersistenceMapper.toEntity(any(SportProfile.class))).thenReturn(testProfileEntity);
        when(fileStorageService.storeFullPhoto(mockFile, 1L)).thenReturn(photoUrl);
        when(sportProfileRepository.save(any(SportProfileEntity.class))).thenReturn(testProfileEntity);
        when(userPersistenceMapper.toModel(testProfileEntity)).thenReturn(profileWithPhoto);
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(expectedResponse);

        ProfileResponse response = playerService.uploadFullPhotoByUserId(1L, mockFile);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(fileStorageService).storeFullPhoto(mockFile, 1L);
        verify(sportProfileRepository).save(any(SportProfileEntity.class));
    }

    @Test
    @DisplayName("Should fail uploading full photo when user not found")
    void testUploadFullPhotoByUserIdUserNotFound() {
        MultipartFile mockFile = mock(MultipartFile.class);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.uploadFullPhotoByUserId(99L, mockFile));
        verify(fileStorageService, never()).storeFullPhoto(any(), any());
    }

}
