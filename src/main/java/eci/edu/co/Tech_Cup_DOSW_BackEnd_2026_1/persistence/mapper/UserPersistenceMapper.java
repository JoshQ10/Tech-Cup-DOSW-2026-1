package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.VerificationToken;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.SportProfileEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.VerificationTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {

    UserEntity toEntity(User model);

    User toModel(UserEntity entity);

    SportProfileEntity toEntity(SportProfile model);

    SportProfile toModel(SportProfileEntity entity);

    VerificationTokenEntity toEntity(VerificationToken model);

    VerificationToken toModel(VerificationTokenEntity entity);
}
