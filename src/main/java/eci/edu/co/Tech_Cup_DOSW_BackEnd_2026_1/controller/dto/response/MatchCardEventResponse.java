package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tarjeta registrada en el partido")
public class MatchCardEventResponse {

    @Schema(description = "Tipo de tarjeta: YELLOW_CARD o RED_CARD")
    private MatchEventType cardType;

    @Schema(description = "ID del jugador")
    private Long playerId;

    @Schema(description = "Nombre completo del jugador")
    private String playerName;

    @Schema(description = "ID del equipo")
    private Long teamId;

    @Schema(description = "Nombre del equipo")
    private String teamName;

    @Schema(description = "Minuto de la tarjeta, incluyendo tiempo de reposición (ej. '34+1')")
    private String minute;
}
