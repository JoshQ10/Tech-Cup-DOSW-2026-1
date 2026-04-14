package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para actualizar perfil deportivo por usuario")
public class SportsProfileUpdateRequest {

    @NotNull(message = "La posición principal es requerida")
    @Schema(description = "Posición principal del jugador", example = "FORWARD", requiredMode = Schema.RequiredMode.REQUIRED)
    private Position primaryPosition;

    @Schema(description = "Posición secundaria del jugador", example = "MIDFIELDER")
    private Position secondaryPosition;

    @NotNull(message = "El dorsal es requerido")
    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
    @Max(value = 99, message = "El dorsal debe ser menor a 100")
    @Schema(description = "Número de camiseta del jugador", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer jerseyNumber;

    @NotNull(message = "El pie dominante es requerido")
    @Schema(description = "Pie dominante del jugador", example = "RIGHT", requiredMode = Schema.RequiredMode.REQUIRED)
    private DominantFoot dominantFoot;

    @NotNull(message = "La disponibilidad es requerida")
    @Schema(description = "Disponibilidad del jugador", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean available;
}