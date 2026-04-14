package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para cambiar el estado de un torneo")
public class ChangeStatusRequest {

    @Schema(description = "Nuevo estado del torneo", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private TournamentStatus status;
}
