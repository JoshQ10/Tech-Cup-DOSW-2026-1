package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Tournament;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class TournamentMapper {

    /**
     * Convierte un TournamentRequest DTO a una entidad Tournament
     *
     * @param request el DTO de solicitud
     * @return la entidad Tournament mapeada
     */
    public Tournament toEntity(TournamentRequest request) {
        log.trace(
                "Mapping TournamentRequest to Tournament entity - name: {}, startDate: {}, endDate: {}, teamCount: {}, costPerTeam: {}",
                request.getName(), request.getStartDate(), request.getEndDate(), request.getTeamCount(),
                request.getCostPerTeam());
        Tournament mapped = mapToEntity(request);
        log.trace("Tournament entity mapped successfully");
        return mapped;
    }

    /**
     * Convierte una entidad Tournament a un TournamentResponse DTO
     *
     * @param entity la entidad Tournament
     * @return el DTO de respuesta mapeado
     */
    public TournamentResponse toResponse(Tournament entity) {
        log.trace("Mapping Tournament entity to TournamentResponse DTO - id: {}, name: {}, status: {}",
                entity.getId(), entity.getName(), entity.getStatus());
        TournamentResponse mapped = mapToResponse(entity);
        log.trace("TournamentResponse DTO mapped successfully");
        return mapped;
    }

    /**
     * Método abstracto para MapStruct
     */
    protected abstract Tournament mapToEntity(TournamentRequest request);

    /**
     * Método abstracto para MapStruct
     */
    protected abstract TournamentResponse mapToResponse(Tournament entity);
}
