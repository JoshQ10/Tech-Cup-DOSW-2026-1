package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para enviar invitación a un jugador")
public class InvitePlayerRequest {

    @Schema(description = "ID del jugador a invitar", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long playerId;
}
