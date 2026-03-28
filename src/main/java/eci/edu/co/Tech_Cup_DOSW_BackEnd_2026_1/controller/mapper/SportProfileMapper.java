package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.SportProfile;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class SportProfileMapper {

    /**
     * Convierte un ProfileRequest DTO a una entidad SportProfile
     *
     * @param request el DTO de solicitud
     * @return la entidad SportProfile mapeada
     */
    public SportProfile toEntity(ProfileRequest request) {
        log.trace("Mapping ProfileRequest to SportProfile entity - position: {}, jerseyNumber: {}, available: {}",
                request.getPosition(), request.getJerseyNumber(), request.isAvailable());
        SportProfile mapped = mapToEntity(request);
        log.trace("SportProfile entity mapped successfully");
        return mapped;
    }

    /**
     * Convierte una entidad SportProfile a un ProfileResponse DTO
     * Mapea el nombre del jugador desde la entidad asociada
     *
     * @param entity la entidad SportProfile
     * @return el DTO de respuesta mapeado
     */
    @Mapping(target = "playerName", ignore = true)
    public ProfileResponse toResponse(SportProfile entity) {
        log.trace(
                "Mapping SportProfile entity to ProfileResponse DTO - id: {}, userId: {}, position: {}, available: {}",
                entity.getId(), entity.getUserId(), entity.getPosition(), entity.isAvailable());
        ProfileResponse mapped = mapToResponse(entity);
        log.trace("ProfileResponse DTO mapped successfully");
        return mapped;
    }

    /**
     * Método abstracto para MapStruct
     */
    @Mapping(target = "id", ignore = true)
    protected abstract SportProfile mapToEntity(ProfileRequest request);

    /**
     * Método abstracto para MapStruct
     */
    @Mapping(target = "playerName", ignore = true)
    protected abstract ProfileResponse mapToResponse(SportProfile entity);
}
