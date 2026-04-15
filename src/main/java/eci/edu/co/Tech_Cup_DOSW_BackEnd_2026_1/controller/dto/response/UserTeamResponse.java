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
@Schema(description = "Equipo actual del jugador o estado sin equipo")
public class UserTeamResponse {

    @Schema(description = "Indica si el jugador pertenece a un equipo activo", example = "true")
    private boolean hasTeam;

    @Schema(description = "ID del equipo (null si no tiene equipo)", example = "3")
    private Long id;

    @Schema(description = "Nombre del equipo (null si no tiene equipo)", example = "Los Tigres FC")
    private String name;

    @Schema(description = "URL del escudo del equipo (null si no tiene equipo)", example = "https://example.com/shield.png")
    private String shieldUrl;

    @Schema(description = "ID del capitán del equipo (null si no tiene equipo)", example = "7")
    private Long captainId;

    @Schema(description = "Nombre completo del capitán (null si no tiene equipo)", example = "Juan Pérez")
    private String captainName;

    @Schema(description = "Lista de IDs de jugadores del equipo (null si no tiene equipo)", example = "[1, 2, 3, 4, 5]")
    private List<Long> players;
}
