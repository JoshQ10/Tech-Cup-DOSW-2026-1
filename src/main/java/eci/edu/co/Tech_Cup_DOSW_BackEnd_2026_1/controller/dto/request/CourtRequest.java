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
@Schema(description = "Request para registrar una cancha del torneo")
public class CourtRequest {

    @Schema(description = "Nombre de la cancha", example = "Cancha Principal", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Ubicacion de la cancha", example = "Bloque B - Piso 1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String location;
}
