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
@Schema(description = "Respuesta paginada con jugadores disponibles")
public class PlayerSearchResponse {

    @Schema(description = "Lista de jugadores disponibles", example = "[]")
    private List<ProfileResponse> players;

    @Schema(description = "Número de página actual (0-indexed)", example = "0")
    private int currentPage;

    @Schema(description = "Número total de elementos encontrados", example = "42")
    private long totalElements;

    @Schema(description = "Número total de páginas", example = "5")
    private int totalPages;

    @Schema(description = "Elementos por página", example = "10")
    private int pageSize;

    @Schema(description = "Indica si hay más páginas disponibles", example = "true")
    private boolean hasNextPage;

    @Schema(description = "Indica si es la primera página", example = "true")
    private boolean isFirstPage;

    @Schema(description = "Indica si es la última página", example = "false")
    private boolean isLastPage;
}
