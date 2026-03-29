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
@Schema(description = "Request para crear un nuevo equipo en un torneo")
public class TeamRequest {

    @Schema(description = "Nombre del equipo", example = "Los Tigres FC", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "URL del escudo del equipo", example = "https://example.com/shield.png")
    private String shieldUrl;

    @Schema(description = "Colores del uniforme del equipo", example = "Azul y Blanco")
    private String uniformColors;

    @Schema(description = "ID del torneo al que pertenece el equipo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tournamentId;

    @Schema(description = "ID del usuario capitán del equipo", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long captainId;
}
