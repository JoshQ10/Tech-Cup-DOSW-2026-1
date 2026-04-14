package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de una invitación recibida por el jugador")
public class InvitationDetailResponse {

    @Schema(description = "ID de la invitación", example = "1")
    private Long id;

    @Schema(description = "ID del equipo que envió la invitación", example = "3")
    private Long teamId;

    @Schema(description = "Nombre del equipo", example = "Los Tigres FC")
    private String teamName;

    @Schema(description = "URL del escudo del equipo", example = "https://example.com/shield.png")
    private String teamShieldUrl;

    @Schema(description = "ID del capitán del equipo", example = "7")
    private Long captainId;

    @Schema(description = "Nombre completo del capitán", example = "Juan Pérez")
    private String captainName;

    @Schema(description = "Número de jugadores actualmente inscritos en el equipo", example = "8")
    private int playersEnrolled;

    @Schema(description = "Cupo total del equipo", example = "15")
    private int totalCapacity;

    @Schema(description = "Fecha y hora en que se envió la invitación")
    private LocalDateTime sentAt;

    @Schema(description = "Estado actual de la invitación", example = "PENDING")
    private InvitationStatus status;
}
