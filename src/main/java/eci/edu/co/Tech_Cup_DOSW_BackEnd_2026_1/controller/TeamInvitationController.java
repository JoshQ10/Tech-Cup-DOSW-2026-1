package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitations", description = "Team invitation management")
public class TeamInvitationController {

    private final TeamInvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List invitations", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) Long playerId) {
        List<TeamInvitationEntity> invitations;
        if (teamId != null) {
            invitations = invitationRepository.findByTeamId(teamId);
        } else if (playerId != null) {
            invitations = invitationRepository.findByPlayerId(playerId);
        } else {
            invitations = invitationRepository.findAll();
        }
        return ResponseEntity.ok(invitations.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get invitation", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        TeamInvitationEntity invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        return ResponseEntity.ok(toMap(invitation));
    }

    @PostMapping
    @PreAuthorize("hasRole('CAPTAIN')")
    @Operation(summary = "Create invitation", description = "Allowed roles: CAPTAIN")
    public ResponseEntity<Map<String, Object>> create(@RequestBody InvitationCreateRequest request,
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
    @Operation(summary = "Update invitation status", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, CAPTAIN, PLAYER")
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
    @Operation(summary = "Delete invitation", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!invitationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invitation not found");
        }
        invitationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
