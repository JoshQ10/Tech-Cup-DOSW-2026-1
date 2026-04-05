package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SportProfileMapper Tests")
class SportProfileMapperTest {

    private SportProfileMapper sportProfileMapper;

    private ProfileRequest profileRequest;
    private SportProfile sportProfileEntity;

    @BeforeEach
    void setUp() {
        sportProfileMapper = Mappers.getMapper(SportProfileMapper.class);

        profileRequest = ProfileRequest.builder()
                .position(Position.FORWARD)
                .jerseyNumber(10)
                .photoUrl("https://example.com/photo.jpg")
                .available(true)
                .semester(5)
                .gender("M")
                .age(20)
                .build();

        sportProfileEntity = SportProfile.builder()
                .id(1L)
                .userId(5L)
                .position(Position.FORWARD)
                .program(Program.SYSTEMS_ENGINEERING)
                .jerseyNumber(10)
                .photoUrl("https://example.com/photo.jpg")
                .available(true)
                .semester(5)
                .gender("M")
                .age(20)
                .build();
    }

    @Test
    @DisplayName("Should convert ProfileRequest to SportProfile entity successfully")
    void testToEntity() {
        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(profileRequest);

        // Assert
        assertThat(mappedProfile)
                .isNotNull()
                .extracting("position", "jerseyNumber", "photoUrl", "available", "semester", "gender", "age")
                .containsExactly(Position.FORWARD, 10, "https://example.com/photo.jpg", true, 5, "M", 20);
    }

    @Test
    @DisplayName("Should convert SportProfile entity to ProfileResponse successfully")
    void testToResponse() {
        // Act
        ProfileResponse response = sportProfileMapper.toResponse(sportProfileEntity);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting("id", "userId", "position", "jerseyNumber", "photoUrl", "available")
                .containsExactly(1L, 5L, Position.FORWARD, 10, "https://example.com/photo.jpg", true);
    }

    @Test
    @DisplayName("Should handle null ProfileRequest in toEntity")
    void testToEntityWithNull() {
        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(null);

        // Assert
        assertThat(mappedProfile).isNull();
    }

    @Test
    @DisplayName("Should handle null SportProfile entity in toResponse")
    void testToResponseWithNull() {
        // Act
        ProfileResponse response = sportProfileMapper.toResponse(null);

        // Assert
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should preserve all fields when mapping ProfileRequest to SportProfile")
    void testToEntityPreservesAllFields() {
        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(profileRequest);

        // Assert
        assertThat(mappedProfile.getPosition()).isEqualTo(profileRequest.getPosition());
        assertThat(mappedProfile.getJerseyNumber()).isEqualTo(profileRequest.getJerseyNumber());
        assertThat(mappedProfile.getPhotoUrl()).isEqualTo(profileRequest.getPhotoUrl());
        assertThat(mappedProfile.isAvailable()).isEqualTo(profileRequest.isAvailable());
        assertThat(mappedProfile.getSemester()).isEqualTo(profileRequest.getSemester());
        assertThat(mappedProfile.getGender()).isEqualTo(profileRequest.getGender());
        assertThat(mappedProfile.getAge()).isEqualTo(profileRequest.getAge());
    }

    @Test
    @DisplayName("Should preserve all fields when mapping SportProfile to ProfileResponse")
    void testToResponsePreservesAllFields() {
        // Act
        ProfileResponse response = sportProfileMapper.toResponse(sportProfileEntity);

        // Assert
        assertThat(response.getId()).isEqualTo(sportProfileEntity.getId());
        assertThat(response.getUserId()).isEqualTo(sportProfileEntity.getUserId());
        assertThat(response.getPosition()).isEqualTo(sportProfileEntity.getPosition());
        assertThat(response.getJerseyNumber()).isEqualTo(sportProfileEntity.getJerseyNumber());
        assertThat(response.getPhotoUrl()).isEqualTo(sportProfileEntity.getPhotoUrl());
        assertThat(response.isAvailable()).isEqualTo(sportProfileEntity.isAvailable());
    }

    @Test
    @DisplayName("Should handle different positions when mapping ProfileRequest")
    void testToEntityWithDifferentPositions() {
        // Arrange
        ProfileRequest defenderRequest = ProfileRequest.builder()
                .position(Position.DEFENDER)
                .jerseyNumber(4)
                .photoUrl("https://example.com/defender.jpg")
                .available(true)
                .semester(6)
                .gender("M")
                .age(21)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(defenderRequest);

        // Assert
        assertThat(mappedProfile.getPosition()).isEqualTo(Position.DEFENDER);
    }

    @Test
    @DisplayName("Should handle unavailable player in SportProfile")
    void testToResponseWithUnavailablePlayer() {
        // Arrange
        SportProfile unavailableProfile = SportProfile.builder()
                .id(2L)
                .userId(6L)
                .position(Position.GOALKEEPER)
                .program(Program.SYSTEMS_ENGINEERING)
                .jerseyNumber(1)
                .photoUrl("https://example.com/goalkeeper.jpg")
                .available(false)
                .semester(4)
                .gender("M")
                .age(19)
                .build();

        // Act
        ProfileResponse response = sportProfileMapper.toResponse(unavailableProfile);

        // Assert
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.getPosition()).isEqualTo(Position.GOALKEEPER);
    }

    @Test
    @DisplayName("Should handle null photoUrl in ProfileRequest")
    void testToEntityWithNullPhotoUrl() {
        // Arrange
        ProfileRequest profileWithoutPhoto = ProfileRequest.builder()
                .position(Position.MIDFIELDER)
                .jerseyNumber(8)
                .photoUrl(null)
                .available(true)
                .semester(5)
                .gender("F")
                .age(20)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(profileWithoutPhoto);

        // Assert
        assertThat(mappedProfile.getPhotoUrl()).isNull();
        assertThat(mappedProfile.getPosition()).isEqualTo(Position.MIDFIELDER);
    }

    @Test
    @DisplayName("Should handle null semester in ProfileRequest")
    void testToEntityWithNullSemester() {
        // Arrange
        ProfileRequest profileWithoutSemester = ProfileRequest.builder()
                .position(Position.FORWARD)
                .jerseyNumber(9)
                .photoUrl("https://example.com/photo.jpg")
                .available(true)
                .semester(null)
                .gender("M")
                .age(22)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(profileWithoutSemester);

        // Assert
        assertThat(mappedProfile.getSemester()).isNull();
    }

    @Test
    @DisplayName("Should handle null age in ProfileRequest")
    void testToEntityWithNullAge() {
        // Arrange
        ProfileRequest profileWithoutAge = ProfileRequest.builder()
                .position(Position.DEFENDER)
                .jerseyNumber(3)
                .photoUrl("https://example.com/photo.jpg")
                .available(true)
                .semester(5)
                .gender("F")
                .age(null)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(profileWithoutAge);

        // Assert
        assertThat(mappedProfile.getAge()).isNull();
    }

    @Test
    @DisplayName("Should map different genders correctly")
    void testToEntityWithDifferentGenders() {
        // Arrange
        ProfileRequest femaleProfile = ProfileRequest.builder()
                .position(Position.FORWARD)
                .jerseyNumber(7)
                .photoUrl("https://example.com/female.jpg")
                .available(true)
                .semester(5)
                .gender("F")
                .age(21)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(femaleProfile);

        // Assert
        assertThat(mappedProfile.getGender()).isEqualTo("F");
    }

    @Test
    @DisplayName("Should handle jersey number edge cases")
    void testToEntityWithDifferentJerseyNumbers() {
        // Arrange
        ProfileRequest highNumberRequest = ProfileRequest.builder()
                .position(Position.MIDFIELDER)
                .jerseyNumber(99)
                .photoUrl("https://example.com/photo.jpg")
                .available(true)
                .semester(5)
                .gender("M")
                .age(20)
                .build();

        // Act
        SportProfile mappedProfile = sportProfileMapper.toEntity(highNumberRequest);

        // Assert
        assertThat(mappedProfile.getJerseyNumber()).isEqualTo(99);
    }

    @Test
    @DisplayName("Should map SportProfile with program information")
    void testToResponseWithProgramInfo() {
        // Arrange
        SportProfile profileWithProgram = SportProfile.builder()
                .id(3L)
                .userId(7L)
                .position(Position.FORWARD)
                .program(Program.AI_ENGINEERING)
                .jerseyNumber(11)
                .photoUrl("https://example.com/ai.jpg")
                .available(true)
                .semester(6)
                .gender("M")
                .age(21)
                .build();

        // Act
        ProfileResponse response = sportProfileMapper.toResponse(profileWithProgram);

        // Assert
        assertThat(response.getUserId()).isEqualTo(7L);
        assertThat(response.getPosition()).isEqualTo(Position.FORWARD);
    }
}
