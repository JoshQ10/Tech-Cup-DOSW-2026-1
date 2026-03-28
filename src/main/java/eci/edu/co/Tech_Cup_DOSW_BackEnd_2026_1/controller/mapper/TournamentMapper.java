package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    /**
     * Convierte un TournamentRequest DTO a una entidad Tournament
     *
     * @param request el DTO de solicitud
     * @return la entidad Tournament mapeada
     */
    Tournament toEntity(TournamentRequest request);

    /**
     * Convierte una entidad Tournament a un TournamentResponse DTO
     *
     * @param entity la entidad Tournament
     * @return el DTO de respuesta mapeado
     */
    TournamentResponse toResponse(Tournament entity);
}
