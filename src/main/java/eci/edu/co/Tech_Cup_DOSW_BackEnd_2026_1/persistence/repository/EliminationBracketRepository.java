package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.EliminationBracketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EliminationBracketRepository extends JpaRepository<EliminationBracketEntity, Long> {
    List<EliminationBracketEntity> findByTournamentId(Long tournamentId);

    List<EliminationBracketEntity> findByTournamentIdOrderByRoundAscMatchPositionAsc(Long tournamentId);

    List<EliminationBracketEntity> findByTournamentIdAndRound(Long tournamentId, String round);
}
