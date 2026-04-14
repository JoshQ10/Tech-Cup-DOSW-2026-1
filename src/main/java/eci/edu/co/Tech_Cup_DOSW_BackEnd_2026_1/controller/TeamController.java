package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.InvitePlayerRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.InvitationService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Teams", description = "Endpoints for team management and roster")
public class TeamController {

        private final TeamService teamService;
        private final InvitationService invitationService;
        private final TeamRepository teamRepository;
        private final UserRepository userRepository;

        @PostMapping
        @PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Create team", description = "Creates a new football team. Allowed roles: CAPTAIN, ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Team successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid data"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "409", description = "Team already exists")
        })
        public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request,
                        Authentication authentication) {
                log.info("REST create team endpoint called");
                assertCaptainOwnsRequestedTeamOnCreate(authentication, request.getCaptainId());
                TeamResponse response = teamService.create(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Update team", description = "Updates team base information. Allowed roles: CAPTAIN, ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Team successfully updated"),
                        @ApiResponse(responseCode = "400", description = "Invalid data"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Team not found")
        })
        public ResponseEntity<TeamResponse> update(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable Long id,
                        @Valid @RequestBody TeamRequest request,
                        Authentication authentication) {
                log.info("REST update team endpoint called for id: {}", id);
                assertCaptainOwnsTeam(authentication, id);
                TeamResponse response = teamService.update(id, request);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get team", description = "Retrieves the details of a team by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Team found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Team not found")
        })
        public ResponseEntity<TeamResponse> getById(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable Long id) {
                log.info("REST get team endpoint called for id: {}", id);
                TeamResponse response = teamService.getById(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/roster")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get team roster", description = "Retrieves the current roster of the team")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Roster successfully retrieved"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Team not found")
        })
        public ResponseEntity<TeamResponse> getRoster(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable Long id) {
                log.info("REST get roster endpoint called for team: {}", id);
                TeamResponse response = teamService.getRoster(id);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}/players/{playerId}")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Remove player from team", description = "Removes a player from the team roster. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Player successfully removed"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Team or player not found")
        })
        public ResponseEntity<TeamResponse> removePlayer(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable("id") Long teamId,
                        @Parameter(description = "ID del jugador a remover", required = true) @PathVariable Long playerId) {
                log.info("REST remove player endpoint called for team: {} and player: {}", teamId, playerId);
                TeamResponse response = teamService.removePlayer(teamId, playerId);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Delete team", description = "Deletes a team. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Team successfully deleted"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Team not found")
        })
        public ResponseEntity<Void> delete(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable Long id) {
                log.info("REST delete team endpoint called for id: {}", id);
                teamService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @PostMapping("/{teamId}/invite")
        @PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Enviar invitación a un jugador", description = "El capitán busca jugadores y les envía invitaciones. Verifica cupo, que el jugador no tenga equipo y que no exista invitación pendiente. Allowed roles: CAPTAIN, ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Invitación enviada exitosamente"),
                        @ApiResponse(responseCode = "400", description = "El equipo no tiene cupo, el jugador ya tiene equipo o ya existe invitación pendiente"),
                        @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
                        @ApiResponse(responseCode = "404", description = "Equipo o jugador no encontrado")
        })
        public ResponseEntity<Void> invitePlayer(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable Long teamId,
                        @RequestBody InvitePlayerRequest request,
                        Authentication authentication) {
                log.info("REST invite player endpoint called for team: {}, player: {}", teamId, request.getPlayerId());
                assertCaptainOwnsTeam(authentication, teamId);
                invitationService.sendInvitation(teamId, request.getPlayerId());
                return ResponseEntity.noContent().build();
        }

        private void assertCaptainOwnsRequestedTeamOnCreate(Authentication authentication, Long captainId) {
                if (!hasRole(authentication, "ROLE_CAPTAIN")) {
                        return;
                }
                UserEntity currentUser = getCurrentUser(authentication);
                if (!currentUser.getId().equals(captainId)) {
                        throw new AccessDeniedException("Como CAPTAIN solo puedes crear equipos donde seas el capitan");
                }
        }

        private void assertCaptainOwnsTeam(Authentication authentication, Long teamId) {
                if (!hasRole(authentication, "ROLE_CAPTAIN")) {
                        return;
                }
                UserEntity currentUser = getCurrentUser(authentication);
                TeamEntity team = teamRepository.findById(teamId)
                                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
                if (team.getCaptainId() == null || !team.getCaptainId().equals(currentUser.getId())) {
                        throw new AccessDeniedException("Como CAPTAIN solo puedes operar sobre tu propio equipo");
                }
        }

        private UserEntity getCurrentUser(Authentication authentication) {
                String email = authentication.getName();
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        }

        private boolean hasRole(Authentication authentication, String roleName) {
                return authentication != null
                                && authentication.getAuthorities().stream()
                                                .anyMatch(a -> roleName.equals(a.getAuthority()));
        }
}
