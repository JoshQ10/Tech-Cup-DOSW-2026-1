package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para registrar una fecha/horario del torneo")
public class TournamentDateRequest {

    @Schema(description = "Descripcion del evento", example = "Jornada 1 - Fase de grupos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Fecha del evento", example = "2026-04-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate eventDate;
}
