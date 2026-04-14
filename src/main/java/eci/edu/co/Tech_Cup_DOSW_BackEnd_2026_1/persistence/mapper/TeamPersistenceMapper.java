package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Payment;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.TeamInvitation;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.PaymentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamPersistenceMapper {

    TeamEntity toEntity(Team model);

    Team toModel(TeamEntity entity);

    TeamInvitationEntity toEntity(TeamInvitation model);

    TeamInvitation toModel(TeamInvitationEntity entity);

    PaymentEntity toEntity(Payment model);

    Payment toModel(PaymentEntity entity);
}
