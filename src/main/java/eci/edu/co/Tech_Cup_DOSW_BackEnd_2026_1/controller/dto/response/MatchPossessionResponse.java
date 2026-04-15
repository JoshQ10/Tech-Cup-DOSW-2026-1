package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de dominación del balón en el partido")
public class MatchPossessionResponse {

    @Schema(description = "Porcentaje de posesión del equipo local")
    private int homePercentage;

    @Schema(description = "Porcentaje de posesión del equipo visitante")
    private int awayPercentage;

    @Schema(description = "Puntos del heatmap de posesión del balón")
    private List<HeatmapPointResponse> heatmapPoints;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Coordenadas de un punto del heatmap")
    public static class HeatmapPointResponse {
        private double x;
        private double y;
    }
}
