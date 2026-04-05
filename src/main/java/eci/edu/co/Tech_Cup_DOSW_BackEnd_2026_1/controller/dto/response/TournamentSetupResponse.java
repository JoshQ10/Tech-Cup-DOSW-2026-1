package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
@Schema(description = "Respuesta con la configuracion completa del torneo")
public class TournamentSetupResponse {

    @Schema(description = "ID del torneo", example = "1")
    private Long tournamentId;

    @Schema(description = "Nombre del torneo", example = "Copa DOSW 2026")
    private String tournamentName;

    @Schema(description = "Reglamento del torneo")
    private String rules;

    @Schema(description = "Fecha limite de inscripcion")
    private LocalDate inscriptionCloseDate;

    @Schema(description = "Reglas de sanciones")
    private String sanctionRules;

    @Schema(description = "Canchas configuradas")
    private List<CourtResponse> courts;

    @Schema(description = "Fechas/horarios programados")
    private List<TournamentDateResponse> schedule;
}
