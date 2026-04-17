package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

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
@Schema(description = "Request para registrar un nuevo usuario en el sistema")
public class RegisterRequest {

    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "Nombre de usuario (username)", example = "juan.perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@escuelaing.edu.co", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Confirmación de contraseña", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmPassword;

    @Schema(description = "Tipo de usuario: INTERNAL (estudiante @escuelaing.edu.co) o EXTERNAL (familiar/invitado)", example = "INTERNAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserType userType;

    @Schema(description = "Tipo de relación (solo para EXTERNAL): FAMILIAR, INVITADO", example = "FAMILIAR")
    private String relationshipType;

    @Schema(description = "Descripción de la relación con la escuela (solo para EXTERNAL)", example = "Padre de estudiante")
    private String relationshipDescription;

    @Schema(description = "Rol asignado al usuario en el sistema", example = "PLAYER", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;

    @Schema(description = "Cédula de identidad (obligatoria para roles ADMINISTRATOR, REFEREE, ORGANIZER)", example = "1234567890")
    private String identification;
}

