package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.RolePermissionRegistry;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.FileStorageService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentDateEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.LineupPlayerRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRulesConfirmationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserManagementController Tests")
class UserManagementControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private SportProfileRepository sportProfileRepository;

        @MockBean
        private MatchEventRepository matchEventRepository;

        @MockBean
        private LineupPlayerRepository lineupPlayerRepository;

        @MockBean
        private TeamRepository teamRepository;

        @MockBean
        private TeamInvitationRepository teamInvitationRepository;

        @MockBean
        private TournamentDateRepository tournamentDateRepository;

        @MockBean
        private TournamentRulesConfirmationRepository tournamentRulesConfirmationRepository;

        @MockBean
        private PlayerService playerService;

        @MockBean
        private FileStorageService fileStorageService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private RolePermissionRegistry rolePermissionRegistry;

        @MockBean
        private TeamService teamService;

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("Should return pending notifications counters")
        void shouldReturnNotificationsCount() throws Exception {
                UserEntity user = UserEntity.builder().id(1L).email("admin@test.com").build();
                TeamEntity team = TeamEntity.builder().id(11L).tournamentId(10L).build();

                TournamentDateEntity pendingDate = TournamentDateEntity.builder()
                                .id(100L)
                                .eventDate(LocalDate.now().plusDays(2))
                                .build();
                TournamentDateEntity oldDate = TournamentDateEntity.builder()
                                .id(101L)
                                .eventDate(LocalDate.now().minusDays(2))
                                .build();

                when(userRepository.findById(1L)).thenReturn(Optional.of(user));
                when(teamInvitationRepository.findByPlayerIdAndStatus(1L, InvitationStatus.PENDING))
                                .thenReturn(List.of(new TeamInvitationEntity(), new TeamInvitationEntity()));
                when(teamRepository.findCurrentTeamByPlayerId(1L)).thenReturn(Optional.of(team));
                when(tournamentDateRepository.findByTournamentId(10L)).thenReturn(List.of(pendingDate, oldDate));
                when(tournamentRulesConfirmationRepository.findByTournamentIdAndUserId(eq(10L), anyLong()))
                                .thenReturn(Optional.empty());

                mockMvc.perform(get("/api/users/1/notifications/count"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userId").value(1))
                                .andExpect(jsonPath("$.pendingInvitations").value(2))
                                .andExpect(jsonPath("$.pendingCalendarEvents").value(1))
                                .andExpect(jsonPath("$.rulesConfirmed").value(false))
                                .andExpect(jsonPath("$.rulesAlert").value(true))
                                .andExpect(jsonPath("$.totalPending").value(4));
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("Should reject privileged role creation without identification")
        void shouldRejectPrivilegedRoleWithoutIdentification() throws Exception {
                Map<String, Object> request = Map.of(
                                "firstName", "Ref",
                                "lastName", "User",
                                "username", "ref.user",
                                "email", "ref@test.com",
                                "password", "strongpass",
                                "role", "REFEREE");

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("Should reject privileged role creation with non-numeric identification")
        void shouldRejectPrivilegedRoleWithNonNumericIdentification() throws Exception {
                Map<String, Object> request = Map.of(
                                "firstName", "Org",
                                "lastName", "User",
                                "username", "org.user",
                                "email", "org@test.com",
                                "password", "strongpass",
                                "role", "ORGANIZER",
                                "identification", "ABC123");

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("Should create privileged role with numeric identification")
        void shouldCreatePrivilegedRoleWithNumericIdentification() throws Exception {
                Map<String, Object> request = Map.of(
                                "firstName", "Admin",
                                "lastName", "User",
                                "username", "admin.user",
                                "email", "admin@test.com",
                                "password", "strongpass",
                                "role", "ADMINISTRATOR",
                                "identification", "987654321");

                when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                        UserEntity toSave = invocation.getArgument(0);
                        toSave.setId(50L);
                        return toSave;
                });

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(50))
                                .andExpect(jsonPath("$.role").value(Role.ADMINISTRATOR.name()));

                ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
                verify(userRepository).save(captor.capture());
                assertEquals("987654321", captor.getValue().getIdentification());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @DisplayName("Should clear identification for non-privileged roles")
        void shouldClearIdentificationForNonPrivilegedRoles() throws Exception {
                Map<String, Object> request = Map.of(
                                "firstName", "Player",
                                "lastName", "User",
                                "username", "player.user",
                                "email", "player@test.com",
                                "password", "strongpass",
                                "role", "PLAYER",
                                "identification", "123456789");

                when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                        UserEntity toSave = invocation.getArgument(0);
                        toSave.setId(44L);
                        return toSave;
                });

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(44))
                                .andExpect(jsonPath("$.role").value(Role.PLAYER.name()));

                ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
                verify(userRepository).save(captor.capture());
                assertNull(captor.getValue().getIdentification());
        }
}
