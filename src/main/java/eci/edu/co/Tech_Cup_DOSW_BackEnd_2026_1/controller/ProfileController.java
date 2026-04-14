package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Profile", description = "Authenticated user profile endpoints")
public class ProfileController {

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get authenticated profile", description = "Returns basic information about the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<Map<String, Object>> profile(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        List<String> roles = authentication != null
                ? authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                : List.of();

        log.info("Authenticated profile requested for user: {}", email);

        return ResponseEntity.ok(Map.of(
                "email", email,
                "roles", roles,
                "authenticated", authentication != null && authentication.isAuthenticated()));
    }
}
