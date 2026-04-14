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
@Schema(description = "Respuesta paginada de invitaciones recibidas")
public class InvitationPageResponse {

    @Schema(description = "Lista de invitaciones")
    private List<InvitationDetailResponse> invitations;

    @Schema(description = "Número de página actual (0-indexed)", example = "0")
    private int currentPage;

    @Schema(description = "Total de invitaciones encontradas", example = "5")
    private long totalElements;

    @Schema(description = "Total de páginas disponibles", example = "1")
    private int totalPages;

    @Schema(description = "Elementos por página", example = "10")
    private int pageSize;

    @Schema(description = "Indica si hay una página siguiente disponible", example = "false")
    private boolean hasNextPage;

    @Schema(description = "Indica si es la primera página", example = "true")
    private boolean isFirstPage;

    @Schema(description = "Indica si es la última página", example = "true")
    private boolean isLastPage;
}
