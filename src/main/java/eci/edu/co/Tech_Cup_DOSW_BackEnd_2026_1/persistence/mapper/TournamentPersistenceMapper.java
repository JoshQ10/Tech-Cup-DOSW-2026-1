package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Court;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.EliminationBracket;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Standing;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.TournamentDate;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.EliminationBracketEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentDateEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TeamPersistenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentPersistenceMapper {

    TournamentEntity toEntity(Tournament model);

    Tournament toModel(TournamentEntity entity);

    TournamentDateEntity toEntity(TournamentDate model);

    TournamentDate toModel(TournamentDateEntity entity);

    CourtEntity toEntity(Court model);

    Court toModel(CourtEntity entity);

    StandingEntity toEntity(Standing model);

    Standing toModel(StandingEntity entity);

    EliminationBracketEntity toEntity(EliminationBracket model);

    EliminationBracket toModel(EliminationBracketEntity entity);
}
