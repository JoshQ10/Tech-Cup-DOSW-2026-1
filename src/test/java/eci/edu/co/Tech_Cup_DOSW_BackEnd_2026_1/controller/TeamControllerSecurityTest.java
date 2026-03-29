package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.config.SecurityConfig;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@Import(SecurityConfig.class)
@DisplayName("TeamController Security Tests")
class TeamControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private TeamRequest teamRequest;
    private TeamResponse teamResponse;

    @BeforeEach
    void setUp() {
        teamRequest = TeamRequest.builder()
                .name("Team Alpha")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Red and Black")
                .tournamentId(1L)
                .captainId(1L)
                .build();

        teamResponse = TeamResponse.builder()
                .id(1L)
                .name("Team Alpha")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Red and Black")
                .tournamentId(1L)
                .captainId(1L)
                .players(List.of(1L, 2L))
                .build();
    }

    // ---- Operaciones permitidas por rol ----

    @Test
    @WithMockUser(roles = "CAPTAIN")
    @DisplayName("CAPTAIN puede crear un equipo")
    void captainCanCreateTeam() throws Exception {
        when(teamService.create(any(TeamRequest.class))).thenReturn(teamResponse);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @DisplayName("ADMINISTRATOR puede crear un equipo")
    void administratorCanCreateTeam() throws Exception {
        when(teamService.create(any(TeamRequest.class))).thenReturn(teamResponse);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "CAPTAIN")
    @DisplayName("CAPTAIN puede remover un jugador del equipo")
    void captainCanRemovePlayer() throws Exception {
        teamResponse.setPlayers(List.of(2L));
        when(teamService.removePlayer(anyLong(), anyLong())).thenReturn(teamResponse);

        mockMvc.perform(delete("/api/teams/1/players/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLAYER")
    @DisplayName("PLAYER puede consultar un equipo")
    void playerCanGetTeam() throws Exception {
        when(teamService.getById(anyLong())).thenReturn(teamResponse);

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("ORGANIZER puede consultar la plantilla de un equipo")
    void organizerCanGetRoster() throws Exception {
        when(teamService.getRoster(anyLong())).thenReturn(teamResponse);

        mockMvc.perform(get("/api/teams/1/roster"))
                .andExpect(status().isOk());
    }

    // ---- Acceso denegado a operaciones sin permisos ----

    @Test
    @WithMockUser(roles = "PLAYER")
    @DisplayName("PLAYER no puede crear un equipo - 403")
    void playerCannotCreateTeam() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("ORGANIZER no puede crear un equipo - 403")
    void organizerCannotCreateTeam() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PLAYER")
    @DisplayName("PLAYER no puede remover un jugador del equipo - 403")
    void playerCannotRemovePlayer() throws Exception {
        mockMvc.perform(delete("/api/teams/1/players/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Usuario no autenticado no puede consultar un equipo - 401")
    void unauthenticatedCannotGetTeam() throws Exception {
        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Usuario no autenticado no puede crear un equipo - 401")
    void unauthenticatedCannotCreateTeam() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isUnauthorized());
    }
}
