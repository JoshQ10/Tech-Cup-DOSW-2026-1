package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para autenticar un usuario con email o username y contrasena")
public class LoginRequest {

    @Schema(description = "Correo electronico del usuario", example = "juan.perez@mail.eci.edu.co")
    private String email;

    @Schema(description = "Nombre de usuario alternativo para login", example = "juan.perez")
    private String username;

    @Schema(description = "Contrasena del usuario", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
