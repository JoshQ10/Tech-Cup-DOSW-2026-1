package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Team;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class TeamMapper {

    /**
     * Convierte un TeamRequest DTO a una entidad Team
     *
     * @param request el DTO de solicitud
     * @return la entidad Team mapeada
     */
    public Team toEntity(TeamRequest request) {
        log.trace("Mapping TeamRequest to Team entity - name: {}, shieldUrl: {}, uniformColors: {}",
                request.getName(), request.getShieldUrl(), request.getUniformColors());
        Team mapped = mapToEntity(request);
        log.trace("Team entity mapped successfully");
        return mapped;
    }

    /**
     * Convierte una entidad Team a un TeamResponse DTO
     *
     * @param entity la entidad Team
     * @return el DTO de respuesta mapeado
     */
    public TeamResponse toResponse(Team entity) {
        log.trace("Mapping Team entity to TeamResponse DTO - id: {}, name: {}, captainId: {}, playersCount: {}",
                entity.getId(), entity.getName(), entity.getCaptainId(),
                entity.getPlayers() != null ? entity.getPlayers().size() : 0);
        TeamResponse mapped = mapToResponse(entity);
        log.trace("TeamResponse DTO mapped successfully");
        return mapped;
    }

    /**
     * Método abstracto para MapStruct
     */
    protected abstract Team mapToEntity(TeamRequest request);

    /**
     * Método abstracto para MapStruct
     */
    protected abstract TeamResponse mapToResponse(Team entity);
}
