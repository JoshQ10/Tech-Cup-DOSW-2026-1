package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

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
@Schema(description = "Request para actualizar el perfil deportivo de un jugador")
public class ProfileRequest {

    @Schema(description = "Posición del jugador en el campo", example = "FORWARD", requiredMode = Schema.RequiredMode.REQUIRED)
    private Position position;

    @Schema(description = "Número de camiseta del jugador", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int jerseyNumber;

    @Schema(description = "URL de la foto del jugador", example = "https://example.com/photo.jpg")
    private String photoUrl;

    @Schema(description = "Indica si el jugador está disponible para jugar", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean available;

    @Schema(description = "Semestre académico del jugador", example = "5")
    private Integer semester;

    @Schema(description = "Género del jugador (M/F)", example = "M")
    private String gender;

    @Schema(description = "Edad del jugador en años", example = "20")
    private Integer age;
}
