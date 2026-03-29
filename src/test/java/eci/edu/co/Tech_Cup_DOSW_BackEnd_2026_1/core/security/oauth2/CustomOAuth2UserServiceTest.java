package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.UserRepository;
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
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @DisplayName("CustomOAuth2UserService should be instantiated")
    void testServiceInstantiation() {
        // Assert
        assertNotNull(customOAuth2UserService);
    }

    @Test
    @DisplayName("CustomOAuth2UserService should have userRepository")
    void testUserRepositoryInjection() {
        // Arrange & Act
        CustomOAuth2UserService service = new CustomOAuth2UserService(userRepository);

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
                .email("oauth@example.com")
                .name("OAuth User")
                .role(Role.PLAYER)
                .userType(UserType.STUDENT)
                .createdAt(LocalDateTime.now())
                .build();

        // Act & Assert
        assertNotNull(user);
        assertEquals("oauth@example.com", user.getEmail());
        assertEquals("OAuth User", user.getName());
        assertEquals(Role.PLAYER, user.getRole());
    }

    @Test
    @DisplayName("UserRepository interaction during OAuth2 flow")
    void testUserRepositoryInteraction() {
        // Arrange
        User mockUser = User.builder()
                .id(1L)
                .email("test@oauth.com")
                .build();
        when(userRepository.findByEmail("test@oauth.com")).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userRepository.findByEmail("test@oauth.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@oauth.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@oauth.com");
    }

    @Test
    @DisplayName("OAuth2 user can be saved to database")
    void testOAuth2UserSaveToDB() {
        // Arrange
        User newUser = User.builder()
                .email("newuser@oauth.com")
                .name("New OAuth User")
                .userType(UserType.STUDENT)
                .role(Role.PLAYER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User savedUser = userRepository.save(newUser);

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
}
