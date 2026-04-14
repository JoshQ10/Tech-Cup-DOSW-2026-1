package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar el resultado de un partido")
public class MatchResultRequest {

    @NotNull
    @Min(0)
    @Schema(description = "Goles del equipo local", example = "2")
    private Integer homeScore;

    @NotNull
    @Min(0)
    @Schema(description = "Goles del equipo visitante", example = "1")
    private Integer awayScore;
}
