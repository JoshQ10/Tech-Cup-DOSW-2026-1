package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationPageResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.InvitationService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitations", description = "Gestión de invitaciones a equipos")
public class TeamInvitationController {

    private final TeamInvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvitationService invitationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar invitaciones recibidas por el jugador", description = "Retorna las invitaciones pendientes del jugador autenticado con datos del equipo. Paginación incluida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de invitaciones obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<InvitationPageResponse> list(
            @Parameter(description = "ID del jugador para filtrar invitaciones") @RequestParam(required = false) Long userId,
            @Parameter(description = "ID del jugador (alias de userId)") @RequestParam(required = false) Long playerId,
            @Parameter(description = "Número de página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de elementos por página", example = "10") @RequestParam(defaultValue = "10") int limit) {

        Long filterUserId = userId != null ? userId : playerId;

        if (filterUserId != null) {
            log.info("REST list pending invitations endpoint called for user: {}, page: {}, limit: {}", filterUserId,
                    page, limit);
            return ResponseEntity.ok(invitationService.listPendingByUser(filterUserId, page, limit));
        }

        log.info("REST list all invitations endpoint called");
        java.util.List<TeamInvitationEntity> all = invitationRepository.findAll();
        java.util.List<eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationDetailResponse> items = all
                .stream().map(this::toDetailResponse).toList();

        InvitationPageResponse response = InvitationPageResponse.builder()
                .invitations(items)
                .currentPage(0)
                .totalElements(items.size())
                .totalPages(1)
                .pageSize(items.size())
                .hasNextPage(false)
                .isFirstPage(true)
                .isLastPage(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','CAPTAIN','PLAYER')")
    @Operation(summary = "Aceptar invitación a un equipo", description = "Valida que el jugador no tenga equipo activo y que el equipo tenga cupo. Asigna el jugador al equipo y cancela las demás invitaciones pendientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Invitación aceptada exitosamente"),
            @ApiResponse(responseCode = "400", description = "El jugador ya tiene equipo o el equipo no tiene cupo"),
            @ApiResponse(responseCode = "403", description = "Solo puedes aceptar tus propias invitaciones"),
            @ApiResponse(responseCode = "404", description = "Invitación no encontrada")
    })
    public ResponseEntity<Void> accept(
            @Parameter(description = "ID de la invitación", required = true) @PathVariable Long id,
            Authentication authentication) {
        log.info("REST accept invitation endpoint called for invitation: {}", id);
        UserEntity currentUser = getCurrentUser(authentication);
        TeamInvitationEntity invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        Long targetPlayerId = currentUser.getId();
        if (hasRole(authentication, "ROLE_ADMINISTRATOR") || hasRole(authentication, "ROLE_ORGANIZER")) {
            targetPlayerId = invitation.getPlayer() != null ? invitation.getPlayer().getId() : null;
        }

        if (targetPlayerId == null) {
            throw new ResourceNotFoundException("Invitation player not found");
        }

        invitationService.acceptInvitation(id, targetPlayerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','CAPTAIN','PLAYER')")
    @Operation(summary = "Rechazar invitación a un equipo", description = "Marca la invitación como rechazada. El jugador permanece sin equipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Invitación rechazada exitosamente"),
            @ApiResponse(responseCode = "400", description = "La invitación ya fue procesada"),
            @ApiResponse(responseCode = "403", description = "Solo puedes rechazar tus propias invitaciones"),
            @ApiResponse(responseCode = "404", description = "Invitación no encontrada")
    })
    public ResponseEntity<Void> reject(
            @Parameter(description = "ID de la invitación", required = true) @PathVariable Long id,
            Authentication authentication) {
        log.info("REST reject invitation endpoint called for invitation: {}", id);
        UserEntity currentUser = getCurrentUser(authentication);
        TeamInvitationEntity invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        Long targetPlayerId = currentUser.getId();
        if (hasRole(authentication, "ROLE_ADMINISTRATOR") || hasRole(authentication, "ROLE_ORGANIZER")) {
            targetPlayerId = invitation.getPlayer() != null ? invitation.getPlayer().getId() : null;
        }

        if (targetPlayerId == null) {
            throw new ResourceNotFoundException("Invitation player not found");
        }

        invitationService.rejectInvitation(id, targetPlayerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener invitación por ID", description = "Retorna el detalle de una invitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitación encontrada"),
            @ApiResponse(responseCode = "404", description = "Invitación no encontrada")
    })
    public ResponseEntity<Map<String, Object>> getById(
            @Parameter(required = true) @PathVariable Long id) {
        TeamInvitationEntity invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        return ResponseEntity.ok(toMap(invitation));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','CAPTAIN')")
    @Operation(summary = "Crear invitación (uso interno)", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, CAPTAIN")
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody InvitationCreateRequest request,
            Authentication authentication) {
        TeamEntity team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        assertCaptainOwnsTeam(authentication, team);
        UserEntity player = userRepository.findById(request.playerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        TeamInvitationEntity invitation = TeamInvitationEntity.builder()
                .team(team)
                .player(player)
                .status(InvitationStatus.PENDING)
                .sentAt(LocalDateTime.now())
                .build();

        TeamInvitationEntity saved = invitationRepository.save(invitation);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','CAPTAIN','PLAYER')")
    @Operation(summary = "Actualizar estado de invitación", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, CAPTAIN, PLAYER")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody InvitationStatusRequest request,
            Authentication authentication) {
        TeamInvitationEntity invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        assertInvitationOwnershipForRole(authentication, invitation);

        invitation.setStatus(InvitationStatus.valueOf(request.status()));
        invitation.setRespondedAt(LocalDateTime.now());
        TeamInvitationEntity updated = invitationRepository.save(invitation);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Eliminar invitación", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!invitationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invitation not found");
        }
        invitationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationDetailResponse toDetailResponse(
            TeamInvitationEntity invitation) {
        TeamEntity team = invitation.getTeam();
        String captainName = null;
        if (team != null && team.getCaptainId() != null) {
            captainName = userRepository.findById(team.getCaptainId())
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .orElse(null);
        }
        return eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationDetailResponse.builder()
                .id(invitation.getId())
                .teamId(team != null ? team.getId() : null)
                .teamName(team != null ? team.getName() : null)
                .teamShieldUrl(team != null ? team.getShieldUrl() : null)
                .captainId(team != null ? team.getCaptainId() : null)
                .captainName(captainName)
                .playersEnrolled(team != null && team.getPlayers() != null ? team.getPlayers().size() : 0)
                .totalCapacity(eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants.MAX_TEAM_PLAYERS)
                .sentAt(invitation.getSentAt())
                .status(invitation.getStatus())
                .build();
    }

    private Map<String, Object> toMap(TeamInvitationEntity invitation) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", invitation.getId());
        data.put("teamId", invitation.getTeam() != null ? invitation.getTeam().getId() : null);
        data.put("playerId", invitation.getPlayer() != null ? invitation.getPlayer().getId() : null);
        data.put("status", invitation.getStatus());
        data.put("sentAt", invitation.getSentAt());
        data.put("respondedAt", invitation.getRespondedAt());
        return data;
    }

    public record InvitationCreateRequest(Long teamId, Long playerId) {
    }

    public record InvitationStatusRequest(String status) {
    }

    private void assertCaptainOwnsTeam(Authentication authentication, TeamEntity team) {
        if (!hasRole(authentication, "ROLE_CAPTAIN")) {
            return;
        }
        UserEntity currentUser = getCurrentUser(authentication);
        if (team.getCaptainId() == null || !team.getCaptainId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Como CAPTAIN solo puedes invitar jugadores a tu propio equipo");
        }
    }

    private void assertInvitationOwnershipForRole(Authentication authentication, TeamInvitationEntity invitation) {
        if (hasRole(authentication, "ROLE_ADMINISTRATOR") || hasRole(authentication, "ROLE_ORGANIZER")) {
            return;
        }

        UserEntity currentUser = getCurrentUser(authentication);
        if (hasRole(authentication, "ROLE_CAPTAIN")) {
            Long teamCaptainId = invitation.getTeam() != null ? invitation.getTeam().getCaptainId() : null;
            if (teamCaptainId == null || !teamCaptainId.equals(currentUser.getId())) {
                throw new AccessDeniedException("Como CAPTAIN solo puedes gestionar invitaciones de tu equipo");
            }
            return;
        }

        if (hasRole(authentication, "ROLE_PLAYER")) {
            Long invitedPlayerId = invitation.getPlayer() != null ? invitation.getPlayer().getId() : null;
            if (invitedPlayerId == null || !invitedPlayerId.equals(currentUser.getId())) {
                throw new AccessDeniedException("Como PLAYER solo puedes responder tus propias invitaciones");
            }
        }
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        return authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> roleName.equals(a.getAuthority()));
    }
}
