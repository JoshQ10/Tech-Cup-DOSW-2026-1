package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.SportProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportProfileMapper {

    /**
     * Convierte un ProfileRequest DTO a una entidad SportProfile
     *
     * @param request el DTO de solicitud
     * @return la entidad SportProfile mapeada
     */
    SportProfile toEntity(ProfileRequest request);

    /**
     * Convierte una entidad SportProfile a un ProfileResponse DTO
     * Mapea el nombre del jugador desde la entidad asociada
     *
     * @param entity la entidad SportProfile
     * @return el DTO de respuesta mapeado
     */
    @Mapping(target = "playerName", ignore = true)
    ProfileResponse toResponse(SportProfile entity);
}
