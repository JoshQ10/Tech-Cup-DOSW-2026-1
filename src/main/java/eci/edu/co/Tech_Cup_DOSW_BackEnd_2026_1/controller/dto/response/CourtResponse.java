package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con la informacion de una cancha")
public class CourtResponse {

    @Schema(description = "ID de la cancha", example = "1")
    private Long id;

    @Schema(description = "Nombre de la cancha", example = "Cancha Principal")
    private String name;

    @Schema(description = "Ubicacion de la cancha", example = "Bloque B - Piso 1")
    private String location;
}
