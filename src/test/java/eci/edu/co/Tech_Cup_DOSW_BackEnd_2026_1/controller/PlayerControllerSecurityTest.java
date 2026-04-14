package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config.SecurityTestConfig;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
@DisplayName("PlayerController Security Tests")
@SuppressWarnings("null")
class PlayerControllerSecurityTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PlayerService playerService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private CustomOAuth2UserService customOAuth2UserService;

        @MockBean
        private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        private ProfileRequest profileRequest;
        private AvailabilityRequest availabilityRequest;
        private ProfileResponse profileResponse;

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
                                .available(true)
                                .build();

                profileResponse = ProfileResponse.builder()
                                .id(1L)
                                .userId(1L)
                                .position(Position.FORWARD)
                                .jerseyNumber(10)
                                .available(true)
                                .build();
        }

        // ---- Operaciones permitidas por rol ----

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("PLAYER puede actualizar su perfil deportivo")
        void playerCanUpdateProfile() throws Exception {
                when(playerService.updateProfile(anyLong(), any(ProfileRequest.class))).thenReturn(profileResponse);

                mockMvc.perform(put("/api/players/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "CAPTAIN")
        @DisplayName("CAPTAIN puede actualizar perfil deportivo")
        void captainCanUpdateProfile() throws Exception {
                when(playerService.updateProfile(anyLong(), any(ProfileRequest.class))).thenReturn(profileResponse);

                mockMvc.perform(put("/api/players/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("PLAYER puede cambiar disponibilidad")
        void playerCanChangeAvailability() throws Exception {
                when(playerService.changeAvailability(anyLong(), any(AvailabilityRequest.class)))
                                .thenReturn(profileResponse);

                mockMvc.perform(patch("/api/players/1/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("Cualquier usuario autenticado puede consultar un perfil")
        void anyAuthenticatedUserCanGetProfile() throws Exception {
                when(playerService.getProfile(anyLong())).thenReturn(profileResponse);

                mockMvc.perform(get("/api/players/1/profile"))
                                .andExpect(status().isOk());
        }

        // ---- Acceso denegado a operaciones sin permisos ----

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("ORGANIZER puede actualizar perfil deportivo")
        void organizerCanUpdateProfile() throws Exception {
                when(playerService.updateProfile(anyLong(), any())).thenReturn(profileResponse);

                mockMvc.perform(put("/api/players/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "REFEREE")
        @DisplayName("REFEREE no puede cambiar disponibilidad de jugador - 403")
        void refereeCannotChangeAvailability() throws Exception {
                mockMvc.perform(patch("/api/players/1/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(availabilityRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Usuario no autenticado no puede acceder a perfil - 401")
        void unauthenticatedCannotGetProfile() throws Exception {
                mockMvc.perform(get("/api/players/1/profile"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Usuario no autenticado no puede actualizar perfil - 401")
        void unauthenticatedCannotUpdateProfile() throws Exception {
                mockMvc.perform(put("/api/players/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(profileRequest)))
                                .andExpect(status().isUnauthorized());
        }
}
