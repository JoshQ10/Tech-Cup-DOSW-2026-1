package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
@Schema(description = "Respuesta con la informacion de una fecha del torneo")
public class TournamentDateResponse {

    @Schema(description = "ID de la fecha", example = "1")
    private Long id;

    @Schema(description = "Descripcion del evento", example = "Jornada 1 - Fase de grupos")
    private String description;

    @Schema(description = "Fecha del evento", example = "2026-04-15")
    private LocalDate eventDate;
}
