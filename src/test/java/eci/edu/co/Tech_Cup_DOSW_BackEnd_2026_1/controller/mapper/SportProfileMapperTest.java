package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.SportProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SportProfileMapper Tests")
class SportProfileMapperTest {

    private SportProfileMapper mapper;
    private ProfileRequest profileRequest;
    private SportProfile sportProfile;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SportProfileMapper.class);
        
        profileRequest = ProfileRequest.builder()
                .position(Position.FORWARD)
                .jerseyNumber(10)
                .photoUrl("http://example.com/photo.jpg")
                .available(true)
                .semester(3)
                .gender("Masculino")
                .age(22)
                .build();

        sportProfile = SportProfile.builder()
                .id(1L)
                .userId(1L)
                .position(Position.MIDFIELDER)
                .jerseyNumber(7)
                .photoUrl("http://example.com/profile.jpg")
                .available(false)
                .semester(4)
                .gender("Femenino")
                .age(21)
                .build();
    }

    @Test
    @DisplayName("Should map ProfileRequest to SportProfile entity")
    void testMapProfileRequestToEntity() {
        // Act
        SportProfile entity = mapper.toEntity(profileRequest);

        // Assert
        assertNotNull(entity);
        assertEquals(Position.FORWARD, entity.getPosition());
        assertEquals(10, entity.getJerseyNumber());
        assertEquals("http://example.com/photo.jpg", entity.getPhotoUrl());
        assertTrue(entity.isAvailable());
        assertEquals(3, entity.getSemester());
        assertEquals("Masculino", entity.getGender());
        assertEquals(22, entity.getAge());
    }

    @Test
    @DisplayName("Should map null ProfileRequest to null")
    void testMapNullProfileRequestToEntity() {
        // Act
        SportProfile entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map SportProfile entity to ProfileResponse DTO")
    void testMapEntityToProfileResponse() {
        // Act
        ProfileResponse response = mapper.toResponse(sportProfile);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(Position.MIDFIELDER, response.getPosition());
        assertEquals(7, response.getJerseyNumber());
        assertEquals("http://example.com/profile.jpg", response.getPhotoUrl());
        assertFalse(response.isAvailable());
    }

    @Test
    @DisplayName("Should map null SportProfile to null")
    void testMapNullEntityToProfileResponse() {
        // Act
        ProfileResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    @DisplayName("Should map SportProfile with different Position")
    void testMapEntityWithDifferentPositions() {
        // Arrange
        sportProfile.setPosition(Position.GOALKEEPER);

        // Act
        ProfileResponse response = mapper.toResponse(sportProfile);

        // Assert
        assertNotNull(response);
        assertEquals(Position.GOALKEEPER, response.getPosition());
    }

    @Test
    @DisplayName("Should map SportProfile with null fields")
    void testMapEntityWithNullFields() {
        // Arrange
        sportProfile.setPhotoUrl(null);

        // Act
        ProfileResponse response = mapper.toResponse(sportProfile);

        // Assert
        assertNotNull(response);
        assertNull(response.getPhotoUrl());
        assertEquals(Position.MIDFIELDER, response.getPosition());
    }

    @Test
    @DisplayName("Should map ProfileRequest with all positions")
    void testMapProfileRequestWithAllPositions() {
        // Test all positions
        Position[] positions = {Position.FORWARD, Position.MIDFIELDER, Position.DEFENDER, Position.GOALKEEPER};
        
        for (Position pos : positions) {
            // Act
            profileRequest.setPosition(pos);
            SportProfile entity = mapper.toEntity(profileRequest);

            // Assert
            assertNotNull(entity);
            assertEquals(pos, entity.getPosition());
        }
    }
}
