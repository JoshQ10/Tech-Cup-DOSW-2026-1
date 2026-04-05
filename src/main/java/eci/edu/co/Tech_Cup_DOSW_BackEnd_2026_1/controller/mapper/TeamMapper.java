package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {

    /**
     * Convierte un TeamRequest DTO a una entidad Team
     *
     * @param request el DTO de solicitud
     * @return la entidad Team mapeada
     */
    Team toEntity(TeamRequest request);

    /**
     * Convierte una entidad Team a un TeamResponse DTO
     *
     * @param entity la entidad Team
     * @return el DTO de respuesta mapeado
     */
    TeamResponse toResponse(Team entity);
}
