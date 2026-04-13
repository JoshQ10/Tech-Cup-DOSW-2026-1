package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para registrar la relacion de un usuario externo con la escuela")
public class SchoolRelationRequest {

    @NotNull(message = "El subtipo de relacion es requerido")
    @Schema(description = "Subtipo de relacion con la escuela", example = "FAMILIAR", requiredMode = Schema.RequiredMode.REQUIRED)
    private SchoolRelationSubtype subtype;

    @NotBlank(message = "La descripcion de la relacion es requerida")
    @Size(min = 5, max = 500, message = "La descripcion debe tener entre 5 y 500 caracteres")
    @Schema(description = "Descripcion libre de la relacion con la institucion", example = "Familiar de un estudiante activo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    public enum SchoolRelationSubtype {
        FAMILIAR,
        INVITADO
    }
}