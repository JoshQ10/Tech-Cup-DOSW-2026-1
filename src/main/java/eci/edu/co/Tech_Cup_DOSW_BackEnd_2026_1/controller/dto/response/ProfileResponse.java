package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el perfil deportivo de un jugador")
public class ProfileResponse {

    @Schema(description = "ID único del perfil deportivo", example = "1")
    private Long id;

    @Schema(description = "ID del usuario propietario del perfil", example = "5")
    private Long userId;

    @Schema(description = "Nombre completo del jugador", example = "Juan Pérez")
    private String playerName;

    @Schema(description = "Posición del jugador en el campo", example = "FORWARD")
    private Position position;

    @Schema(description = "Número de camiseta del jugador", example = "10")
    private int jerseyNumber;

    @Schema(description = "URL de la foto del jugador", example = "https://example.com/photo.jpg")
    private String photoUrl;

    @Schema(description = "Indica si el jugador está disponible para jugar", example = "true")
    private boolean available;
}
