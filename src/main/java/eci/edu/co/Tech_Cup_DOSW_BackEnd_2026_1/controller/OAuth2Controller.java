package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth/oauth2")
@Tag(name = "OAuth2", description = "Endpoints para manejar el flujo OAuth2")
public class OAuth2Controller {

    @GetMapping("/google")
    @Operation(summary = "Iniciar autenticacion con Google", description = "Retorna la URL para iniciar el flujo OAuth2 con Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL de autorización generada")
    })
    public ResponseEntity<Map<String, String>> googleAuthorizationUrl() {
        String authorizationUrl = "/oauth2/authorization/google";
        log.info("OAuth2 Google authorization URL requested");
        return ResponseEntity.ok(Map.of("authorizationUrl", authorizationUrl));
    }

    @GetMapping("/success")
    @Operation(summary = "Callback exitoso OAuth2", description = "Recibe tokens emitidos por la API después del login OAuth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación OAuth2 completada"),
            @ApiResponse(responseCode = "400", description = "Faltan tokens en callback")
    })
    public ResponseEntity<?> success(
            @RequestParam(required = false) String accessToken,
            @RequestParam(required = false) String refreshToken,
            @RequestParam(required = false, defaultValue = "Bearer") String tokenType) {

        if (accessToken == null || accessToken.isBlank()) {
            log.warn("OAuth2 success callback without access token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Missing access token in OAuth2 callback"));
        }

        LoginResponse response = LoginResponse.builder()
                .token(accessToken)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();

        log.info("OAuth2 callback success endpoint consumed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failure")
    @Operation(summary = "Callback fallido OAuth2", description = "Retorna detalle de error en autenticación OAuth2")
    public ResponseEntity<Map<String, String>> failure(
            @RequestParam(required = false, defaultValue = "OAuth2 authentication failed") String error) {
        log.warn("OAuth2 authentication failed: {}", error);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", error));
    }
}
