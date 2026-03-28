package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convierte un RegisterRequest DTO a una entidad User
     *
     * @param request el DTO de registro
     * @return la entidad User mapeada
     */
    User toEntity(RegisterRequest request);

    /**
     * Convierte una entidad User a un UserResponse DTO
     *
     * @param entity la entidad User
     * @return el DTO de respuesta mapeado
     */
    UserResponse toResponse(User entity);

    /**
     * Convierte una entidad User a un UserResponse DTO
     * Método sobrecargado para mayor flexibilidad
     *
     * @param entity la entidad User
     * @return el DTO de respuesta mapeado
     */
    default UserResponse toUserResponse(User entity) {
        return toResponse(entity);
    }
}
