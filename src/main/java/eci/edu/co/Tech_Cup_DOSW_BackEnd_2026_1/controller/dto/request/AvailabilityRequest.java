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
@Schema(description = "Request para actualizar la disponibilidad de un jugador")
public class AvailabilityRequest {

    @Schema(description = "Indica si el jugador está disponible para jugar", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean available;
}
