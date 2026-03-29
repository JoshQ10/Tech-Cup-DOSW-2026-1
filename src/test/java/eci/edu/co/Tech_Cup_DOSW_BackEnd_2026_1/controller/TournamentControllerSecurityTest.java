package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.config.SecurityConfig;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
@Import(SecurityConfig.class)
@DisplayName("TournamentController Security Tests")
class TournamentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private JwtService jwtService;

    private TournamentRequest tournamentRequest;
    private TournamentConfigRequest configRequest;
    private ChangeStatusRequest changeStatusRequest;
    private TournamentResponse tournamentResponse;

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
    }

    // ---- Operaciones permitidas por rol ----

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
        when(tournamentService.configure(anyLong(), any(TournamentConfigRequest.class))).thenReturn(tournamentResponse);

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
        when(tournamentService.changeStatus(anyLong(), any(ChangeStatusRequest.class))).thenReturn(tournamentResponse);

        mockMvc.perform(patch("/api/tournaments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeStatusRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "REFEREE")
    @DisplayName("REFEREE puede cambiar el estado de un torneo")
    void refereeCanChangeStatus() throws Exception {
        when(tournamentService.changeStatus(anyLong(), any(ChangeStatusRequest.class))).thenReturn(tournamentResponse);

        mockMvc.perform(patch("/api/tournaments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeStatusRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLAYER")
    @DisplayName("Cualquier usuario autenticado puede consultar un torneo")
    void anyAuthenticatedUserCanGetTournament() throws Exception {
        when(tournamentService.getById(anyLong())).thenReturn(tournamentResponse);

        mockMvc.perform(get("/api/tournaments/1"))
                .andExpect(status().isOk());
    }

    // ---- Acceso denegado a operaciones sin permisos ----

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
}
