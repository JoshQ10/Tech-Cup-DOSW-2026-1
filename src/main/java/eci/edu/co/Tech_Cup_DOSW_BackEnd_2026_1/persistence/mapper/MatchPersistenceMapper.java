package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.Lineup;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.LineupPlayer;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.Match;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.MatchEvent;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.Sanction;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupPlayerEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.SanctionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class, TeamPersistenceMapper.class, TournamentPersistenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchPersistenceMapper {

    MatchEntity toEntity(Match model);

    Match toModel(MatchEntity entity);

    LineupEntity toEntity(Lineup model);

    Lineup toModel(LineupEntity entity);

    LineupPlayerEntity toEntity(LineupPlayer model);

    LineupPlayer toModel(LineupPlayerEntity entity);

    MatchEventEntity toEntity(MatchEvent model);

    MatchEvent toModel(MatchEventEntity entity);

    SanctionEntity toEntity(Sanction model);

    Sanction toModel(SanctionEntity entity);
}
