package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
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
@Schema(description = "Solicitud para registrar tarjetas de un partido")
public class MatchCardRequest {

    @NotEmpty
    @Valid
    @Schema(description = "Lista de tarjetas a registrar")
    private List<CardEntry> cards;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Datos de una tarjeta")
    public static class CardEntry {

        @NotNull
        @Schema(description = "ID del jugador sancionado", example = "7")
        private Long playerId;

        @NotNull
        @Schema(description = "ID del equipo", example = "3")
        private Long teamId;

        @NotNull
        @Schema(description = "Tipo de tarjeta: YELLOW_CARD o RED_CARD")
        private MatchEventType cardType;

        @NotNull
        @Min(1)
        @Schema(description = "Minuto de la tarjeta", example = "34")
        private Integer minute;

        @Min(0)
        @Schema(description = "Minutos de reposición adicionales", example = "0")
        private int additionalMinutes;
    }
}
