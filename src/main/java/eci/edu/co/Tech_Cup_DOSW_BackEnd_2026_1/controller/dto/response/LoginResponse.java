package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con tokens JWT y datos del usuario")
public class LoginResponse {

    @Schema(description = "Token JWT de acceso (alias de accessToken)", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Token JWT de acceso con vigencia corta", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Token JWT de refresco con vigencia larga", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;

    @Schema(description = "Tipo de token, siempre Bearer", example = "Bearer")
    private String tokenType;

    @Schema(description = "Información básica del usuario autenticado")
    private UserResponse user;
}
