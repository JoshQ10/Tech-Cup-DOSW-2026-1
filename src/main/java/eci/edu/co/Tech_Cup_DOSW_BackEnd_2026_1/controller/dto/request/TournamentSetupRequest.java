package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para configurar reglamento, canchas, horarios y sanciones de un torneo")
public class TournamentSetupRequest {

    @Schema(description = "Reglamento del torneo", example = "Partidos de 2 tiempos de 25 minutos. Tarjeta roja = expulsion inmediata.")
    private String rules;

    @Schema(description = "Fecha limite de inscripcion", example = "2026-03-20")
    private LocalDate inscriptionCloseDate;

    @Schema(description = "Reglas de sanciones aplicables", example = "Tarjeta amarilla acumulada (2) = 1 partido. Tarjeta roja = 2 partidos.")
    private String sanctionRules;

    @Schema(description = "Lista de canchas disponibles para el torneo")
    private List<CourtRequest> courts;

    @Schema(description = "Lista de fechas/horarios programados para el torneo")
    private List<TournamentDateRequest> schedule;
}
