package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con la información de un equipo")
public class TeamResponse {

    @Schema(description = "ID único del equipo", example = "1")
    private Long id;

    @Schema(description = "Nombre del equipo", example = "Los Tigres FC")
    private String name;

    @Schema(description = "URL del escudo del equipo", example = "https://example.com/shield.png")
    private String shieldUrl;

    @Schema(description = "Colores del uniforme del equipo", example = "Azul y Blanco")
    private String uniformColors;

    @Schema(description = "ID del torneo al que pertenece el equipo", example = "1")
    private Long tournamentId;

    @Schema(description = "ID del usuario capitán del equipo", example = "5")
    private Long captainId;

    @Schema(description = "Lista de IDs de los jugadores del equipo", example = "[1, 2, 3, 4, 5]")
    private List<Long> players;
}
