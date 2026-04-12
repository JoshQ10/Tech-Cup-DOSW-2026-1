package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config.SecurityTestConfig;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityTestConfig.class)
@DisplayName("JWT Token Flow Tests")
class JwtTokenFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    @DisplayName("Flujo correcto JWT: token válido permite acceso a endpoint protegido")
    void validTokenGrantsAccess() throws Exception {
        ProfileResponse profileResponse = ProfileResponse.builder()
                .id(1L).userId(1L).position(Position.FORWARD).jerseyNumber(10).available(true).build();
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.isAccessToken(anyString())).thenReturn(true);
        when(jwtService.extractEmail(anyString())).thenReturn("player@test.com");
        when(jwtService.extractRole(anyString())).thenReturn("PLAYER");
        when(playerService.getProfile(anyLong())).thenReturn(profileResponse);

        mockMvc.perform(get("/api/players/1/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Flujo token expirado JWT: token expirado retorna 401")
    void expiredTokenReturnsUnauthorized() throws Exception {
        when(jwtService.isTokenValid(anyString()))
                .thenThrow(new io.jsonwebtoken.ExpiredJwtException(null, null, "JWT expired"));

        mockMvc.perform(get("/api/players/1/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer expired-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Flujo token inválido JWT: token con firma incorrecta retorna 401")
    void invalidTokenReturnsUnauthorized() throws Exception {
        when(jwtService.isTokenValid(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/players/1/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Flujo sin token JWT: sin cabecera Authorization retorna 401")
    void noTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/players/1/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Flujo token inválido JWT: token mal formado lanza excepción y retorna 401")
    void malformedTokenReturnsUnauthorized() throws Exception {
        when(jwtService.isTokenValid(anyString()))
                .thenThrow(new io.jsonwebtoken.MalformedJwtException("Malformed JWT token"));

        mockMvc.perform(get("/api/players/1/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer malformed.token"))
                .andExpect(status().isUnauthorized());
    }
}
