package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.SchoolRelationRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.SportsProfileUpdateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserCompleteProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserTeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.FileStorageService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.SportProfileEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.LineupPlayerRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRulesConfirmationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management and profiles")
@SuppressWarnings("null")
public class UserManagementController {

    private static final Set<Role> PRIVILEGED_ROLES_WITH_IDENTIFICATION = Set.of(
            Role.REFEREE,
            Role.ORGANIZER,
            Role.ADMINISTRATOR);
    private static final Pattern IDENTIFICATION_NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    private final UserRepository userRepository;
    private final SportProfileRepository sportProfileRepository;
    private final MatchEventRepository matchEventRepository;
    private final LineupPlayerRepository lineupPlayerRepository;
    private final TeamRepository teamRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final TournamentDateRepository tournamentDateRepository;
    private final TournamentRulesConfirmationRepository tournamentRulesConfirmationRepository;
    private final PlayerService playerService;
    private final FileStorageService fileStorageService;
    private final TeamService teamService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List users", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> listUsers(Authentication authentication) {
        List<Map<String, Object>> result;
        if (hasRole(authentication, "ROLE_PLAYER") || hasRole(authentication, "ROLE_CAPTAIN")) {
            result = List.of(toUserSummary(getCurrentUser(authentication)));
        } else {
            result = userRepository.findAll().stream().map(this::toUserSummary).toList();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getUser(
            @Parameter(required = true) @PathVariable Long id,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
        return ResponseEntity.ok(toUserSummary(user));
    }

    @GetMapping("/{id}/notifications/count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get pending notifications count", description = "Returns pending invitations count, pending calendar events count and rules confirmation status")
    public ResponseEntity<NotificationCountResponse> getNotificationsCount(
            @Parameter(required = true) @PathVariable Long id,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        long pendingInvitations = teamInvitationRepository.findByPlayerIdAndStatus(id, InvitationStatus.PENDING).size();

        TeamEntity currentTeam = teamRepository.findCurrentTeamByPlayerId(id).orElse(null);
        long pendingCalendarEvents = 0L;
        boolean rulesConfirmed = true;

        if (currentTeam != null && currentTeam.getTournamentId() != null) {
            Long tournamentId = currentTeam.getTournamentId();
            LocalDate today = LocalDate.now();

            pendingCalendarEvents = tournamentDateRepository.findByTournamentId(tournamentId).stream()
                    .filter(date -> date.getEventDate() != null && !date.getEventDate().isBefore(today))
                    .count();

            rulesConfirmed = tournamentRulesConfirmationRepository
                    .findByTournamentIdAndUserId(tournamentId, user.getId())
                    .isPresent();
        }

        long totalPending = pendingInvitations + pendingCalendarEvents + (rulesConfirmed ? 0 : 1);

        NotificationCountResponse response = new NotificationCountResponse(
                user.getId(),
                pendingInvitations,
                pendingCalendarEvents,
                rulesConfirmed,
                !rulesConfirmed,
                totalPending);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get complete user profile", description = "Returns personal, sports and statistics data of a user. Allowed roles: all authenticated")
    public ResponseEntity<UserCompleteProfileResponse> getCompleteProfile(
            @Parameter(required = true) @PathVariable Long id,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        SportProfileEntity sportProfile = sportProfileRepository.findByUserId(id).orElse(null);
        TeamEntity currentTeam = teamRepository.findCurrentTeamByPlayerId(id).orElse(null);

        long goals = matchEventRepository.countByPlayerIdAndEventType(id, MatchEventType.GOAL);
        long assists = 0L;
        long matchesPlayed = lineupPlayerRepository.countPlayedMatchesByPlayerId(id);

        UserCompleteProfileResponse response = UserCompleteProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .userType(user.getUserType())
                .relationshipType(user.getRelationshipType())
                .relationshipDescription(user.getRelationshipDescription())
                .sportsProfile(sportProfile == null ? null
                        : UserCompleteProfileResponse.SportsProfileInfo.builder()
                                .profileId(sportProfile.getId())
                                .primaryPosition(sportProfile.getPosition())
                                .secondaryPosition(sportProfile.getSecondaryPosition())
                                .jerseyNumber(sportProfile.getJerseyNumber())
                                .dominantFoot(sportProfile.getDominantFoot())
                                .available(sportProfile.isAvailable())
                                .photoUrl(sportProfile.getPhotoUrl())
                                .fullPhotoUrl(sportProfile.getFullPhotoUrl())
                                .build())
                .statistics(UserCompleteProfileResponse.UserStatsInfo.builder()
                        .goals(goals)
                        .assists(assists)
                        .matchesPlayed(matchesPlayed)
                        .build())
                .currentTeam(currentTeam == null ? null
                        : UserCompleteProfileResponse.CurrentTeamInfo.builder()
                                .teamId(currentTeam.getId())
                                .name(currentTeam.getName())
                                .tournamentId(currentTeam.getTournamentId())
                                .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/team")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener equipo actual del jugador", description = "Retorna los datos del equipo al que pertenece actualmente el usuario (nombre, escudo, capitán, jugadores) o estado 'sin equipo'.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Datos del equipo o estado sin equipo retornados exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserTeamResponse> getUserTeam(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        log.info("REST get user team endpoint called for user: {}", id);
        UserTeamResponse response = teamService.getUserTeam(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create user", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserWriteRequest request) {
        Role targetRole = request.role() == null ? Role.PLAYER : request.role();
        String normalizedIdentification = normalizeIdentificationForRole(targetRole, request.identification());

        UserEntity user = UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(request.password() == null || request.password().isBlank() ? "changeme" : request.password())
                .role(targetRole)
                .userType(request.userType() == null ? UserType.EXTERNAL : request.userType())
                .program(request.program())
                .identification(normalizedIdentification)
                .relationshipType(request.relationshipType())
                .relationshipDescription(request.relationshipDescription())
                .createdAt(LocalDateTime.now())
                .build();

        user.setActive(request.active() == null || request.active());

        UserEntity saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toUserSummary(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','REFEREE','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Update user", description = "Allowed roles: PLAYER, CAPTAIN, REFEREE, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> updateUser(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody UserWriteRequest request,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);
        if ((hasRole(authentication, "ROLE_PLAYER") || hasRole(authentication, "ROLE_CAPTAIN")
                || hasRole(authentication, "ROLE_REFEREE"))
                && (request.role() != null || request.userType() != null || request.active() != null)) {
            throw new AccessDeniedException(
                    "PLAYER/CAPTAIN/REFEREE no pueden cambiar rol, tipo de usuario ni estado activo");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        if (request.firstName() != null)
            user.setFirstName(request.firstName());
        if (request.lastName() != null)
            user.setLastName(request.lastName());
        if (request.username() != null)
            user.setUsername(request.username());
        if (request.email() != null)
            user.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank())
            user.setPassword(request.password());
        if (request.role() != null)
            user.setRole(request.role());
        if (request.userType() != null)
            user.setUserType(request.userType());
        if (request.program() != null)
            user.setProgram(request.program());
        if (request.role() != null || request.identification() != null) {
            Role effectiveRole = request.role() != null ? request.role() : user.getRole();
            String effectiveIdentification = request.identification() != null ? request.identification()
                    : user.getIdentification();
            user.setIdentification(normalizeIdentificationForRole(effectiveRole, effectiveIdentification));
        }
        if (request.relationshipType() != null)
            user.setRelationshipType(request.relationshipType());
        if (request.relationshipDescription() != null)
            user.setRelationshipDescription(request.relationshipDescription());
        if (request.active() != null)
            user.setActive(request.active());

        UserEntity updated = userRepository.save(user);
        return ResponseEntity.ok(toUserSummary(updated));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update user role", description = "Assigns or updates a user's role. Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @Parameter(required = true) @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request,
            Authentication authentication) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        user.setIdentification(normalizeIdentificationForRole(request.role(), user.getIdentification()));
        user.setRole(request.role());
        UserEntity updated = userRepository.save(user);
        return ResponseEntity.ok(toUserSummary(updated));
    }

    @PutMapping("/{id}/sports-profile")
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Update sports profile by user", description = "Stores main position, secondary position, jersey number, dominant foot and availability linked to the user. Allowed roles: PLAYER, CAPTAIN, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<ProfileResponse> updateSportsProfile(
            @Parameter(required = true) @PathVariable Long id,
            @Valid @RequestBody SportsProfileUpdateRequest request,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        if (user.getRole() != Role.PLAYER && user.getRole() != Role.CAPTAIN) {
            throw new AccessDeniedException("Solo se permite perfil deportivo para usuarios PLAYER o CAPTAIN");
        }

        ProfileResponse response = playerService.upsertSportsProfileByUserId(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','REFEREE','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Upload avatar", description = "Receives avatar image (multipart/form-data), validates format and size, stores it and saves URL in user. Allowed roles: PLAYER, CAPTAIN, REFEREE, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @Parameter(required = true) @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        String avatarUrl = fileStorageService.storeAvatar(file, id);
        user.setAvatarUrl(avatarUrl);
        UserEntity updated = userRepository.save(user);
        return ResponseEntity.ok(toUserSummary(updated));
    }

    @PostMapping(value = "/{id}/full-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','REFEREE','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Upload full body photo", description = "Receives full body image (multipart/form-data), validates required dimensions 1428x2920, stores it and saves URL in sports profile. Allowed roles: PLAYER, CAPTAIN, REFEREE, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<ProfileResponse> uploadFullPhoto(
            @Parameter(required = true) @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        if (user.getRole() != Role.PLAYER && user.getRole() != Role.CAPTAIN && user.getRole() != Role.REFEREE) {
            throw new AccessDeniedException(
                    "Solo se permite foto de cuerpo completo para usuarios PLAYER, CAPTAIN o REFEREE");
        }

        ProfileResponse response = playerService.uploadFullPhotoByUserId(id, file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/school-relation")
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','REFEREE','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Register school relation", description = "Stores external user subtype and relation description. Allowed roles: PLAYER, CAPTAIN, REFEREE, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> registerSchoolRelation(
            @Parameter(required = true) @PathVariable Long id,
            @Valid @RequestBody SchoolRelationRequest request,
            Authentication authentication) {
        assertOwnUserForRestrictedRoles(authentication, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        if (user.getUserType() != UserType.EXTERNAL) {
            throw new IllegalArgumentException("Solo usuarios EXTERNAL pueden registrar relacion con la escuela");
        }

        user.setRelationshipType(request.getSubtype().name());
        user.setRelationshipDescription(request.getDescription().trim());

        UserEntity updated = userRepository.save(user);
        return ResponseEntity.ok(toUserSummary(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete user", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> deleteUser(
            @Parameter(required = true) @PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toUserSummary(UserEntity user) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", user.getId());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("avatarUrl", user.getAvatarUrl());
        data.put("role", user.getRole());
        data.put("userType", user.getUserType());
        data.put("program", user.getProgram());
        data.put("relationshipType", user.getRelationshipType());
        data.put("relationshipDescription", user.getRelationshipDescription());
        data.put("active", user.isActive());
        return data;
    }

    public record UserWriteRequest(
            String firstName,
            String lastName,
            String username,
            String email,
            String password,
            Role role,
            UserType userType,
            Program program,
            String identification,
            String relationshipType,
            String relationshipDescription,
            Boolean active) {
    }

    public record RoleUpdateRequest(@NotNull(message = "El rol es requerido") Role role) {
    }

    public record NotificationCountResponse(
            Long userId,
            long pendingInvitations,
            long pendingCalendarEvents,
            boolean rulesConfirmed,
            boolean rulesAlert,
            long totalPending) {
    }

    private void assertOwnUserForRestrictedRoles(Authentication authentication, Long targetUserId) {
        if (!hasRole(authentication, "ROLE_PLAYER")
                && !hasRole(authentication, "ROLE_CAPTAIN")
                && !hasRole(authentication, "ROLE_REFEREE")) {
            return;
        }
        UserEntity currentUser = getCurrentUser(authentication);
        if (!currentUser.getId().equals(targetUserId)) {
            throw new AccessDeniedException("Solo puedes consultar/editar tu propio perfil");
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

    private String normalizeIdentificationForRole(Role role, String identification) {
        if (role == null) {
            return identification;
        }

        if (!PRIVILEGED_ROLES_WITH_IDENTIFICATION.contains(role)) {
            return null;
        }

        if (identification == null || identification.isBlank()) {
            throw new IllegalArgumentException(
                    "La identificación es obligatoria para roles REFEREE, ORGANIZER y ADMINISTRATOR");
        }

        String normalized = identification.trim();
        if (!IDENTIFICATION_NUMERIC_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("La identificación debe contener únicamente números");
        }

        return normalized;
    }
}
