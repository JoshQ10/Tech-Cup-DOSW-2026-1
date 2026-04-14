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
@Schema(description = "Request para configurar las fechas y parámetros de un torneo")
public class TournamentConfigRequest {

    @Schema(description = "Fecha de inicio del torneo", example = "2026-03-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;

    @Schema(description = "Fecha de finalización del torneo", example = "2026-05-31", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate endDate;

    @Schema(description = "Número máximo de equipos participantes", example = "16", requiredMode = Schema.RequiredMode.REQUIRED)
    private int teamCount;

    @Schema(description = "Costo de inscripción por equipo en pesos colombianos", example = "500000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private double costPerTeam;
}
