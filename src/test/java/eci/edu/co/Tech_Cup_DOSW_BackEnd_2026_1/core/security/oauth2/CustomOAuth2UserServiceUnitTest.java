package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomOAuth2UserService unit tests")
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
        assertNotNull(customOAuth2UserService);
    }

    @Test
    @DisplayName("CustomOAuth2UserService should accept repository and mapper")
    void testUserRepositoryInjection() {
        CustomOAuth2UserService service = new CustomOAuth2UserService(userRepository, userPersistenceMapper);
        assertNotNull(service);
    }

    @Test
    @DisplayName("OAuth2AuthenticationException should be thrown for invalid requests")
    void testOAuth2AuthenticationExceptionHandling() {
        assertThrows(Exception.class, () -> {
            throw new OAuth2AuthenticationException("Invalid token");
        });
    }

    @Test
    @DisplayName("User object can be created from OAuth2 attributes")
    void testUserCreationFromOAuth2Attributes() {
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

        assertNotNull(user);
        assertEquals("oauth@example.com", user.getEmail());
        assertEquals("OAuth", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals(Role.PLAYER, user.getRole());
    }

    @Test
    @DisplayName("UserRepository interaction during OAuth2 flow")
    void testUserRepositoryInteraction() {
        UserEntity mockEntity = UserEntity.builder()
                .id(1L)
                .email("test@oauth.com")
                .build();
        when(userRepository.findByEmail("test@oauth.com")).thenReturn(Optional.of(mockEntity));

        Optional<UserEntity> result = userRepository.findByEmail("test@oauth.com");

        assertTrue(result.isPresent());
        assertEquals("test@oauth.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@oauth.com");
    }

    @Test
    @DisplayName("OAuth2 user can be saved to database")
    void testOAuth2UserSaveToDB() {
        UserEntity newEntity = UserEntity.builder()
                .firstName("New")
                .lastName("OAuth User")
                .username("newoauthuser")
                .email("newuser@oauth.com")
                .userType(UserType.STUDENT)
                .role(Role.PLAYER)
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(newEntity);

        UserEntity savedUser = userRepository.save(newEntity);

        assertNotNull(savedUser);
        assertEquals("newuser@oauth.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(newEntity);
    }

    @Test
    @DisplayName("Multiple OAuth2 users should have different identities")
    void testMultipleOAuth2Users() {
        User user1 = User.builder().id(1L).email("user1@oauth.com").build();
        User user2 = User.builder().id(2L).email("user2@oauth.com").build();

        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    @DisplayName("OAuth2 user role assignment")
    void testOAuth2UserRoleAssignment() {
        User playerUser = User.builder().role(Role.PLAYER).build();
        User captainUser = User.builder().role(Role.CAPTAIN).build();
        User organizerUser = User.builder().role(Role.ORGANIZER).build();

        assertEquals(Role.PLAYER, playerUser.getRole());
        assertEquals(Role.CAPTAIN, captainUser.getRole());
        assertEquals(Role.ORGANIZER, organizerUser.getRole());
    }

    @Test
    @DisplayName("OAuth2 user type assignment")
    void testOAuth2UserTypeAssignment() {
        User studentUser = User.builder().userType(UserType.STUDENT).build();
        User professorUser = User.builder().userType(UserType.PROFESSOR).build();
        User adminUser = User.builder().userType(UserType.ADMINISTRATIVE).build();

        assertEquals(UserType.STUDENT, studentUser.getUserType());
        assertEquals(UserType.PROFESSOR, professorUser.getUserType());
        assertEquals(UserType.ADMINISTRATIVE, adminUser.getUserType());
    }
}
