package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para restablecer la contrasena con token")
public class ResetPasswordRequest {

    @NotBlank(message = "El token es requerido")
    @Schema(description = "Token de recuperacion", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank(message = "La nueva contrasena es requerida")
    @Schema(description = "Nueva contrasena", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;

    @NotBlank(message = "La confirmacion de contrasena es requerida")
    @Schema(description = "Confirmacion de nueva contrasena", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmPassword;
}
