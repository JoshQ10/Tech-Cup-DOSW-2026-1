package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para login con Google OAuth2")
public class GoogleLoginRequest {

    @NotBlank(message = "El token de Google es requerido")
    @Schema(description = "Google ID Token obtenido del cliente", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEifQ...")
    private String idToken;
}
