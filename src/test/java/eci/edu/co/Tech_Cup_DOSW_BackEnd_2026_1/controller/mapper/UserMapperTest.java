package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper mapper;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
        
        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Should map RegisterRequest to User entity")
    void testMapRegisterRequestToEntity() {
        // Act
        User entity = mapper.toEntity(registerRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("John Doe", entity.getName());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("password123", entity.getPassword());
    }

    @Test
    @DisplayName("Should map null RegisterRequest to null")
    void testMapNullRegisterRequestToEntity() {
        // Act
        User entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map User entity to UserResponse DTO")
    void testMapEntityToUserResponse() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane@example.com")
                .build();

        // Act
        UserResponse response = mapper.toResponse(user);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Jane Smith", response.getName());
        assertEquals("jane@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Should map null User to null")
    void testMapNullEntityToUserResponse() {
        // Act
        UserResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    @DisplayName("Should map RegisterRequest with special characters")
    void testMapRegisterRequestWithSpecialCharacters() {
        // Arrange
        registerRequest.setName("José María Pérez");
        registerRequest.setEmail("jose.maria@example.com");

        // Act
        User entity = mapper.toEntity(registerRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("José María Pérez", entity.getName());
        assertEquals("jose.maria@example.com", entity.getEmail());
    }
}
