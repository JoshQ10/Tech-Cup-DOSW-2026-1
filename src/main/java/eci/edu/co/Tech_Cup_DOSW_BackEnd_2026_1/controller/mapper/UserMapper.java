package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class UserMapper {

    /**
     * Convierte un RegisterRequest DTO a una entidad User
     *
     * @param request el DTO de registro
     * @return la entidad User mapeada
     */
    public User toEntity(RegisterRequest request) {
        log.trace("Mapping RegisterRequest to User entity - name: {}, email: {}, role: {}",
                request.getName(), request.getEmail(), request.getRole());
        User mapped = mapToEntity(request);
        log.trace("User entity mapped successfully");
        return mapped;
    }

    /**
     * Convierte una entidad User a un UserResponse DTO
     *
     * @param entity la entidad User
     * @return el DTO de respuesta mapeado
     */
    public UserResponse toResponse(User entity) {
        log.trace("Mapping User entity to UserResponse DTO - id: {}, email: {}, role: {}",
                entity.getId(), entity.getEmail(), entity.getRole());
        UserResponse mapped = mapToResponse(entity);
        log.trace("UserResponse DTO mapped successfully");
        return mapped;
    }

    /**
     * Método abstracto para MapStruct
     */
    protected abstract User mapToEntity(RegisterRequest request);

    /**
     * Método abstracto para MapStruct
     */
    protected abstract UserResponse mapToResponse(User entity);
}
