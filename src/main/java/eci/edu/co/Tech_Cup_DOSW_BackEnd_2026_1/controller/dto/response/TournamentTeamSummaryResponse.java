package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de un equipo inscrito en el torneo")
public class TournamentTeamSummaryResponse {

    @Schema(description = "ID del equipo", example = "1")
    private Long id;

    @Schema(description = "Nombre del equipo", example = "Los Tigres FC")
    private String name;

    @Schema(description = "URL del escudo del equipo", example = "https://example.com/shield.png")
    private String shieldUrl;

    @Schema(description = "ID del capitán del equipo", example = "7")
    private Long captainId;

    @Schema(description = "Nombre completo del capitán", example = "Juan Pérez")
    private String captainName;

    @Schema(description = "Número de jugadores inscritos en el equipo", example = "11")
    private int playerCount;
}
