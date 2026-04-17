package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.VerificationTokenEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.VerificationTokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/dev")
@RequiredArgsConstructor
@Profile("dev-http")
@Tag(name = "Authentication - Dev", description = "Dev diagnostics for registration and persistence")
public class AuthDevController {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @GetMapping("/user-status")
    @Operation(summary = "Consultar estado de usuario por email (dev)", description = "Permite verificar en Swagger si el usuario existe en base de datos y el estado del token de verificacion")
    public ResponseEntity<Map<String, Object>> userStatus(
            @Parameter(description = "Email del usuario a consultar", required = true) @RequestParam String email) {

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("email", email);

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("exists", false);
            response.put("message", "User not found");
            return ResponseEntity.ok(response);
        }

        UserEntity user = userOpt.get();
        response.put("exists", true);
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("active", user.isActive());
        response.put("role", user.getRole());
        response.put("userType", user.getUserType());
        response.put("createdAt", user.getCreatedAt());

        Optional<VerificationTokenEntity> tokenOpt = verificationTokenRepository
                .findByUserIdAndVerifiedFalse(user.getId());
        response.put("hasPendingVerificationToken", tokenOpt.isPresent());

        tokenOpt.ifPresent(token -> {
            response.put("verificationToken", token.getToken());
            response.put("verificationTokenCreatedAt", token.getCreatedAt());
            response.put("verificationTokenExpiresAt", token.getExpiresAt());
            response.put("verificationTokenVerified", token.isVerified());
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent-users")
    @Operation(summary = "Listar usuarios recientes (dev)", description = "Devuelve los ultimos usuarios creados para facilitar validaciones desde Swagger")
    public ResponseEntity<Map<String, Object>> recentUsers(
            @Parameter(description = "Cantidad maxima de usuarios", example = "20") @RequestParam(defaultValue = "20") int limit) {

        int safeLimit = Math.min(Math.max(limit, 1), 100);

        List<Map<String, Object>> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .limit(safeLimit)
                .map(this::toUserRow)
                .toList();

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("count", users.size());
        response.put("users", users);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> toUserRow(UserEntity user) {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("id", user.getId());
        row.put("email", user.getEmail());
        row.put("username", user.getUsername());
        row.put("active", user.isActive());
        row.put("role", user.getRole());
        row.put("userType", user.getUserType());
        row.put("createdAt", user.getCreatedAt());
        return row;
    }
}
