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
@Schema(description = "Respuesta paginada de partidos del equipo")
public class MatchPageResponse {

    private List<MatchSummaryResponse> matches;
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private int pageSize;
    private boolean hasNextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
}
