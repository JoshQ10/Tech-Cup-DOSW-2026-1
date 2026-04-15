package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config.SecurityTestConfig;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.RolePermissionRegistry;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
@DisplayName("TournamentController Security Tests")
@SuppressWarnings("null")
class TournamentControllerSecurityTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private TournamentService tournamentService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private RolePermissionRegistry rolePermissionRegistry;

        @MockBean
        private CustomOAuth2UserService customOAuth2UserService;

        @MockBean
        private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        @MockBean
        private TeamService teamService;

        private TournamentRequest tournamentRequest;
        private TournamentConfigRequest configRequest;
        private ChangeStatusRequest changeStatusRequest;
        private TournamentSetupRequest setupRequest;
        private TournamentResponse tournamentResponse;
        private TournamentSetupResponse setupResponse;

        @BeforeEach
        void setUp() {
                tournamentRequest = TournamentRequest.builder()
                                .name("Copa DOSW 2026")
                                .startDate(LocalDate.of(2026, 4, 1))
                                .endDate(LocalDate.of(2026, 6, 30))
                                .teamCount(8)
                                .costPerTeam(300000.0)
                                .build();

                configRequest = TournamentConfigRequest.builder()
                                .startDate(LocalDate.of(2026, 4, 1))
                                .endDate(LocalDate.of(2026, 6, 30))
                                .teamCount(8)
                                .costPerTeam(300000.0)
                                .build();

                changeStatusRequest = ChangeStatusRequest.builder()
                                .status(TournamentStatus.ACTIVE)
                                .build();

                tournamentResponse = TournamentResponse.builder()
                                .id(1L)
                                .name("Copa DOSW 2026")
                                .startDate(LocalDate.of(2026, 4, 1))
                                .endDate(LocalDate.of(2026, 6, 30))
                                .teamCount(8)
                                .costPerTeam(300000.0)
                                .status(TournamentStatus.DRAFT)
                                .build();

                setupRequest = TournamentSetupRequest.builder()
                                .rules("Partidos de 2 tiempos de 25 minutos.")
                                .sanctionRules("Tarjeta roja = 2 partidos")
                                .courts(List.of(CourtRequest.builder().name("Cancha 1").location("Bloque B").build()))
                                .schedule(List.of(TournamentDateRequest.builder()
                                                .description("Jornada 1").eventDate(LocalDate.of(2026, 4, 15)).build()))
                                .build();

                setupResponse = TournamentSetupResponse.builder()
                                .tournamentId(1L)
                                .tournamentName("Copa DOSW 2026")
                                .rules("Partidos de 2 tiempos de 25 minutos.")
                                .build();
        }

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("ORGANIZER puede crear un torneo")
        void organizerCanCreateTournament() throws Exception {
                when(tournamentService.create(any(TournamentRequest.class))).thenReturn(tournamentResponse);

                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("ADMINISTRATOR puede crear un torneo")
        void administratorCanCreateTournament() throws Exception {
                when(tournamentService.create(any(TournamentRequest.class))).thenReturn(tournamentResponse);

                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("ORGANIZER puede configurar un torneo")
        void organizerCanConfigureTournament() throws Exception {
                when(tournamentService.configure(anyLong(), any(TournamentConfigRequest.class)))
                                .thenReturn(tournamentResponse);

                mockMvc.perform(put("/api/tournaments/1/config")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(configRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("ORGANIZER puede cambiar el estado de un torneo")
        void organizerCanChangeStatus() throws Exception {
                tournamentResponse.setStatus(TournamentStatus.ACTIVE);
                when(tournamentService.changeStatus(anyLong(), any(ChangeStatusRequest.class)))
                                .thenReturn(tournamentResponse);

                mockMvc.perform(patch("/api/tournaments/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "REFEREE")
        @DisplayName("REFEREE no puede cambiar el estado de un torneo - 403")
        void refereeCannotChangeStatus() throws Exception {
                mockMvc.perform(patch("/api/tournaments/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("Cualquier usuario autenticado puede consultar un torneo")
        void anyAuthenticatedUserCanGetTournament() throws Exception {
                when(tournamentService.getById(anyLong())).thenReturn(tournamentResponse);

                mockMvc.perform(get("/api/tournaments/1"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("PLAYER no puede crear un torneo - 403")
        void playerCannotCreateTournament() throws Exception {
                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "CAPTAIN")
        @DisplayName("CAPTAIN no puede crear un torneo - 403")
        void captainCannotCreateTournament() throws Exception {
                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("PLAYER no puede configurar un torneo - 403")
        void playerCannotConfigureTournament() throws Exception {
                mockMvc.perform(put("/api/tournaments/1/config")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(configRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "CAPTAIN")
        @DisplayName("CAPTAIN no puede cambiar el estado de un torneo - 403")
        void captainCannotChangeStatus() throws Exception {
                mockMvc.perform(patch("/api/tournaments/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Usuario no autenticado no puede consultar un torneo - 401")
        void unauthenticatedCannotGetTournament() throws Exception {
                mockMvc.perform(get("/api/tournaments/1"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Usuario no autenticado no puede crear un torneo - 401")
        void unauthenticatedCannotCreateTournament() throws Exception {
                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ORGANIZER")
        @DisplayName("ORGANIZER puede configurar (setup) un torneo")
        void organizerCanSetupTournament() throws Exception {
                when(tournamentService.setup(anyLong(), any(TournamentSetupRequest.class))).thenReturn(setupResponse);

                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("ADMINISTRATOR puede configurar (setup) un torneo")
        void administratorCanSetupTournament() throws Exception {
                when(tournamentService.setup(anyLong(), any(TournamentSetupRequest.class))).thenReturn(setupResponse);

                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "PLAYER")
        @DisplayName("PLAYER no puede configurar (setup) un torneo - 403")
        void playerCannotSetupTournament() throws Exception {
                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Usuario no autenticado no puede configurar (setup) un torneo - 401")
        void unauthenticatedCannotSetupTournament() throws Exception {
                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isUnauthorized());
        }
}
