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
@Schema(description = "Gol registrado en el partido")
public class MatchGoalEventResponse {

    @Schema(description = "ID del jugador")
    private Long playerId;

    @Schema(description = "Nombre completo del jugador")
    private String playerName;

    @Schema(description = "ID del equipo")
    private Long teamId;

    @Schema(description = "Nombre del equipo")
    private String teamName;

    @Schema(description = "Minuto del gol, incluyendo tiempo de reposición (ej. '45+2')")
    private String minute;
}
