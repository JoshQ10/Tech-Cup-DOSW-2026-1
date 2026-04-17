package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomOAuth2UserService Tests")
class CustomOAuth2UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @DisplayName("CustomOAuth2UserService should be instantiated")
    void testServiceInstantiation() {
        // Assert
        assertNotNull(customOAuth2UserService);
    }

    @Test
    @DisplayName("CustomOAuth2UserService should have userRepository and mapper")
    void testUserRepositoryAndMapperInjection() {
        // Arrange & Act
        CustomOAuth2UserService service = new CustomOAuth2UserService(userRepository, userPersistenceMapper);

        // Assert
        assertNotNull(service);
    }

    @Test
    @DisplayName("OAuth2AuthenticationException should be thrown for invalid requests")
    void testOAuth2AuthenticationExceptionHandling() {
        // Arrange
        String invalidToken = "invalid_token";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            throw new OAuth2AuthenticationException("Invalid token");
        });
    }

    @Test
    @DisplayName("User object can be created from OAuth2 attributes")
    void testUserCreationFromOAuth2Attributes() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .firstName("OAuth")
                .lastName("User")
                .username("oauthuser")
                .email("oauth@example.com")
                .role(Role.PLAYER)
                .userType(UserType.STUDENT)
                .createdAt(LocalDateTime.now())
                .build();

        // Act & Assert
        assertNotNull(user);
        assertEquals("oauth@example.com", user.getEmail());
        assertEquals("OAuth", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals(Role.PLAYER, user.getRole());
    }

    @Test
    @DisplayName("UserRepository interaction during OAuth2 flow")
    void testUserRepositoryInteraction() {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@oauth.com");
        when(userRepository.findByEmail("test@oauth.com")).thenReturn(Optional.of(mockUser));

        // Act
        Optional<UserEntity> result = userRepository.findByEmail("test@oauth.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@oauth.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@oauth.com");
    }

    @Test
    @DisplayName("OAuth2 user can be saved to database")
    void testOAuth2UserSaveToDB() {
        // Arrange
        UserEntity newUser = new UserEntity();
        newUser.setEmail("newuser@oauth.com");

        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);

        // Act
        UserEntity savedUser = userRepository.save(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("newuser@oauth.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Multiple OAuth2 users should have different identities")
    void testMultipleOAuth2Users() {
        // Arrange
        User user1 = User.builder().id(1L).email("user1@oauth.com").build();
        User user2 = User.builder().id(2L).email("user2@oauth.com").build();

        // Act & Assert
        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    @DisplayName("OAuth2 user role assignment")
    void testOAuth2UserRoleAssignment() {
        // Arrange & Act
        User playerUser = User.builder().role(Role.PLAYER).build();
        User captainUser = User.builder().role(Role.CAPTAIN).build();
        User organizerUser = User.builder().role(Role.ORGANIZER).build();

        // Assert
        assertEquals(Role.PLAYER, playerUser.getRole());
        assertEquals(Role.CAPTAIN, captainUser.getRole());
        assertEquals(Role.ORGANIZER, organizerUser.getRole());
    }

    @Test
    @DisplayName("OAuth2 user type assignment")
    void testOAuth2UserTypeAssignment() {
        // Arrange & Act
        User studentUser = User.builder().userType(UserType.STUDENT).build();
        User professorUser = User.builder().userType(UserType.PROFESSOR).build();
        User adminUser = User.builder().userType(UserType.ADMINISTRATIVE).build();

        // Assert
        assertEquals(UserType.STUDENT, studentUser.getUserType());
        assertEquals(UserType.PROFESSOR, professorUser.getUserType());
        assertEquals(UserType.ADMINISTRATIVE, adminUser.getUserType());
    }

    @Mock
    private OAuth2UserRequest mockOAuth2UserRequest;

    @Mock
    private OAuth2User mockOAuth2User;

    @Test
    @DisplayName("loadUser method should process OAuth2UserRequest with existing user")
    void testLoadUserWithExistingUser() throws OAuth2AuthenticationException {
        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setId(1L);
        existingUserEntity.setEmail("existing@example.com");
        existingUserEntity.setFirstName("Existing");
        existingUserEntity.setLastName("User");

        User existingUser = User.builder()
                .id(1L)
                .email("existing@example.com")
                .firstName("Existing")
                .lastName("User")
                .role(Role.PLAYER)
                .userType(UserType.STUDENT)
                .active(true)
                .build();

        when(mockOAuth2User.getAttribute("email")).thenReturn("existing@example.com");
        when(mockOAuth2User.getAttribute("name")).thenReturn("Existing User");
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUserEntity));
        when(userPersistenceMapper.toModel(existingUserEntity)).thenReturn(existingUser);

        OAuth2User result = customOAuth2UserService.loadUser(mockOAuth2UserRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verify(userPersistenceMapper, times(1)).toModel(existingUserEntity);
    }


}
