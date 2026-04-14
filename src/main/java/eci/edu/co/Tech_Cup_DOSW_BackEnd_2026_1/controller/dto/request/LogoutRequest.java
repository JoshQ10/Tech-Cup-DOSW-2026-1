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
@Schema(description = "Request para cerrar sesion e invalidar refresh token")
public class LogoutRequest {

    @NotBlank(message = "El refresh token es requerido")
    @Schema(description = "Refresh token que se va a invalidar", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}
