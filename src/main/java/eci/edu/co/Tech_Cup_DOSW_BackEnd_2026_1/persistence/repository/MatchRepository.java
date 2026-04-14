package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findByTournamentId(Long tournamentId);

    List<MatchEntity> findByTournamentIdAndPhase(Long tournamentId, MatchPhase phase);

    List<MatchEntity> findByRefereeId(Long refereeId);

    List<MatchEntity> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
}
