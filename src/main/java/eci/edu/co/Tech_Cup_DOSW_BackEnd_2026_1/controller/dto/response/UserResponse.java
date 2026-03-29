package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
@Schema(description = "Respuesta con la información básica de un usuario")
public class UserResponse {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.eci.edu.co")
    private String email;

    @Schema(description = "Rol del usuario en el sistema", example = "PLAYER")
    private Role role;

    @Schema(description = "Indica si la cuenta del usuario está activa", example = "true")
    private boolean active;
}
