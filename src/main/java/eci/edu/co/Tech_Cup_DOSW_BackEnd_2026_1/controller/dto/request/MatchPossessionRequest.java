package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar la dominación del balón en un partido")
public class MatchPossessionRequest {

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Porcentaje de posesión del equipo local", example = "55")
    private Integer homePercentage;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "Porcentaje de posesión del equipo visitante", example = "45")
    private Integer awayPercentage;

    @Valid
    @Schema(description = "Puntos del heatmap de posesión del balón")
    private List<HeatmapPointDto> heatmapPoints;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Coordenadas de un punto del heatmap")
    public static class HeatmapPointDto {

        @Schema(description = "Coordenada X en el campo (0.0 - 1.0)", example = "0.35")
        private double x;

        @Schema(description = "Coordenada Y en el campo (0.0 - 1.0)", example = "0.72")
        private double y;
    }
}
