package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management and profiles")
public class UserManagementController {

    private final UserRepository userRepository;

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
        assertOwnUserForPlayerOrCaptain(authentication, id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
        return ResponseEntity.ok(toUserSummary(user));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Create user", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserWriteRequest request) {
        UserEntity user = UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(request.password() == null || request.password().isBlank() ? "changeme" : request.password())
                .role(request.role() == null ? Role.PLAYER : request.role())
                .userType(request.userType() == null ? UserType.EXTERNAL : request.userType())
                .program(request.program())
                .identification(request.identification())
                .relationshipType(request.relationshipType())
                .relationshipDescription(request.relationshipDescription())
                .active(request.active() == null || request.active())
                .createdAt(LocalDateTime.now())
                .build();

        UserEntity saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toUserSummary(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLAYER','CAPTAIN','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Update user", description = "Allowed roles: PLAYER, CAPTAIN, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> updateUser(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody UserWriteRequest request,
            Authentication authentication) {
        assertOwnUserForPlayerOrCaptain(authentication, id);
        if ((hasRole(authentication, "ROLE_PLAYER") || hasRole(authentication, "ROLE_CAPTAIN"))
                && (request.role() != null || request.userType() != null || request.active() != null)) {
            throw new AccessDeniedException("PLAYER/CAPTAIN no pueden cambiar rol, tipo de usuario ni estado activo");
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
        if (request.identification() != null)
            user.setIdentification(request.identification());
        if (request.relationshipType() != null)
            user.setRelationshipType(request.relationshipType());
        if (request.relationshipDescription() != null)
            user.setRelationshipDescription(request.relationshipDescription());
        if (request.active() != null)
            user.setActive(request.active());

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
        data.put("role", user.getRole());
        data.put("userType", user.getUserType());
        data.put("program", user.getProgram());
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

    private void assertOwnUserForPlayerOrCaptain(Authentication authentication, Long targetUserId) {
        if (!hasRole(authentication, "ROLE_PLAYER") && !hasRole(authentication, "ROLE_CAPTAIN")) {
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
}
