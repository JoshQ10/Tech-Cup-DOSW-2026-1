package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    private RegisterRequest registerRequest;
    private User userEntity;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        registerRequest = RegisterRequest.builder()
                .name("Juan Pérez García")
                .email("juan.perez@example.com")
                .password("SecurePass123!")
                .role(Role.PLAYER)
                .build();

        userEntity = User.builder()
                .id(1L)
                .name("Juan Pérez García")
                .email("juan.perez@example.com")
                .password("SecurePass123!")
                .role(Role.PLAYER)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should convert RegisterRequest to User entity successfully")
    void testToEntity() {
        // Act
        User mappedUser = userMapper.toEntity(registerRequest);

        // Assert
        assertThat(mappedUser)
                .isNotNull()
                .extracting("name", "email", "password", "role")
                .containsExactly("Juan Pérez García", "juan.perez@example.com", "SecurePass123!", Role.PLAYER);
    }

    @Test
    @DisplayName("Should convert User entity to UserResponse successfully")
    void testToResponse() {
        // Act
        UserResponse response = userMapper.toResponse(userEntity);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting("id", "name", "email", "role", "active")
                .containsExactly(1L, "Juan Pérez García", "juan.perez@example.com", Role.PLAYER, true);
    }

    @Test
    @DisplayName("Should handle null RegisterRequest in toEntity")
    void testToEntityWithNull() {
        // Act
        User mappedUser = userMapper.toEntity(null);

        // Assert
        assertThat(mappedUser).isNull();
    }

    @Test
    @DisplayName("Should handle null User entity in toResponse")
    void testToResponseWithNull() {
        // Act
        UserResponse response = userMapper.toResponse(null);

        // Assert
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should preserve all fields when mapping RegisterRequest to User")
    void testToEntityPreservesAllFields() {
        // Act
        User mappedUser = userMapper.toEntity(registerRequest);

        // Assert
        assertThat(mappedUser.getName()).isEqualTo(registerRequest.getName());
        assertThat(mappedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(mappedUser.getPassword()).isEqualTo(registerRequest.getPassword());
        assertThat(mappedUser.getRole()).isEqualTo(registerRequest.getRole());
    }

    @Test
    @DisplayName("Should preserve all fields when mapping User to UserResponse")
    void testToResponsePreservesAllFields() {
        // Act
        UserResponse response = userMapper.toResponse(userEntity);

        // Assert
        assertThat(response.getId()).isEqualTo(userEntity.getId());
        assertThat(response.getName()).isEqualTo(userEntity.getName());
        assertThat(response.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(response.getRole()).isEqualTo(userEntity.getRole());
        assertThat(response.isActive()).isEqualTo(userEntity.isActive());
    }

    @Test
    @DisplayName("Should handle different roles when mapping RegisterRequest to User")
    void testToEntityWithDifferentRoles() {
        // Arrange
        RegisterRequest adminRequest = RegisterRequest.builder()
                .name("Admin User")
                .email("admin@example.com")
                .password("AdminPass123!")
                .role(Role.ADMINISTRATOR)
                .build();

        // Act
        User mappedUser = userMapper.toEntity(adminRequest);

        // Assert
        assertThat(mappedUser.getRole()).isEqualTo(Role.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Should map empty strings correctly (non-null fields)")
    void testToEntityWithEmptyStrings() {
        // Arrange
        RegisterRequest emptyRequest = RegisterRequest.builder()
                .name("")
                .email("")
                .password("")
                .role(Role.PLAYER)
                .build();

        // Act
        User mappedUser = userMapper.toEntity(emptyRequest);

        // Assert
        assertThat(mappedUser.getName()).isEmpty();
        assertThat(mappedUser.getEmail()).isEmpty();
        assertThat(mappedUser.getPassword()).isEmpty();
    }
}
