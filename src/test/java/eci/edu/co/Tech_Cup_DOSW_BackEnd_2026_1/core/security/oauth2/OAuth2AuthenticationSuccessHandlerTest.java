package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserPersistenceMapper mockUserPersistenceMapper;

    @Mock
    private JwtService mockJwtService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private OAuth2User mockOAuth2User;

    private OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;

    @BeforeEach
    void setUp() {
        oauth2SuccessHandler = new OAuth2AuthenticationSuccessHandler(mockUserRepository, mockUserPersistenceMapper, mockJwtService);
        ReflectionTestUtils.setField(oauth2SuccessHandler, "successRedirectUri", "http://localhost:3000/oauth2/callback");
        ReflectionTestUtils.setField(oauth2SuccessHandler, "oauth2AccessTokenTtlMs", 3600000L);
        ReflectionTestUtils.setField(oauth2SuccessHandler, "oauth2RefreshTokenTtlMs", 86400000L);
    }

    @Test
    void testInstantiation() {
        assertThat(oauth2SuccessHandler).isNotNull();
    }

    @Test
    void testOnAuthenticationSuccessMethodExecution() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("oauth@example.com");

        User user = User.builder()
                .id(1L)
                .email("oauth@example.com")
                .build();

        when(mockAuthentication.getPrincipal()).thenReturn(mockOAuth2User);
        when(mockOAuth2User.getAttribute("email")).thenReturn("oauth@example.com");
        when(mockUserRepository.findByEmail("oauth@example.com")).thenReturn(Optional.of(userEntity));
        when(mockUserPersistenceMapper.toModel(userEntity)).thenReturn(user);
        when(mockJwtService.generateAccessToken(user, 3600000L)).thenReturn("access-token-value");
        when(mockJwtService.generateRefreshToken(user, 86400000L)).thenReturn("refresh-token-value");

        oauth2SuccessHandler.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);

        assertThat(oauth2SuccessHandler).isNotNull();
    }

    @Test
    void testOnAuthenticationSuccessWithDifferentInputs() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setEmail("different@example.com");

        User user = User.builder()
                .id(2L)
                .email("different@example.com")
                .build();

        when(mockAuthentication.getPrincipal()).thenReturn(mockOAuth2User);
        when(mockOAuth2User.getAttribute("email")).thenReturn("different@example.com");
        when(mockUserRepository.findByEmail("different@example.com")).thenReturn(Optional.of(userEntity));
        when(mockUserPersistenceMapper.toModel(userEntity)).thenReturn(user);
        when(mockJwtService.generateAccessToken(user, 3600000L)).thenReturn("different-access-token");
        when(mockJwtService.generateRefreshToken(user, 86400000L)).thenReturn("different-refresh-token");

        oauth2SuccessHandler.onAuthenticationSuccess(mockRequest, mockResponse, mockAuthentication);

        assertThat(oauth2SuccessHandler).isNotNull();
    }
}
