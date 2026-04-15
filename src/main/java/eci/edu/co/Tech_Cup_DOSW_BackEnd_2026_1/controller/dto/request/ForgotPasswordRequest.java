package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para solicitar recuperacion de contrasena")
public class ForgotPasswordRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El formato del email es invalido")
    @Schema(description = "Correo de la cuenta", example = "usuario@escuelaing.edu.co", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
