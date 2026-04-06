package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con la información básica de un usuario")
public class UserResponse {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @Schema(description = "Nombre de usuario (username)", example = "juan.perez")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@escuelaing.edu.co")
    private String email;

    @Schema(description = "Rol del usuario en el sistema", example = "PLAYER")
    private Role role;

    @Schema(description = "Tipo de usuario: INTERNAL (estudiante) o EXTERNAL (familiar/invitado)", example = "INTERNAL")
    private UserType userType;

    @Schema(description = "Tipo de relación con la escuela (para externos)", example = "FAMILIAR")
    private String relationshipType;

    @Schema(description = "Indica si la cuenta del usuario está activa", example = "false")
    private boolean active;
}

