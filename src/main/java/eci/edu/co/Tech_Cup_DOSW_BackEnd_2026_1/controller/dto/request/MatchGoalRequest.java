package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar goles de un partido")
public class MatchGoalRequest {

    @NotEmpty
    @Valid
    @Schema(description = "Lista de goles a registrar")
    private List<GoalEntry> goals;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Datos de un gol")
    public static class GoalEntry {

        @NotNull
        @Schema(description = "ID del jugador que anotó", example = "5")
        private Long playerId;

        @NotNull
        @Schema(description = "ID del equipo", example = "3")
        private Long teamId;

        @NotNull
        @Min(1)
        @Schema(description = "Minuto del gol", example = "45")
        private Integer minute;

        @Min(0)
        @Schema(description = "Minutos de reposición adicionales", example = "2")
        private int additionalMinutes;
    }
}
