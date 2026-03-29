package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para registrar un nuevo usuario en el sistema")
public class RegisterRequest {

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.eci.edu.co", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Rol asignado al usuario en el sistema", example = "PLAYER", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;
}
