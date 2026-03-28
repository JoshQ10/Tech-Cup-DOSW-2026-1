package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.EliminationBracket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EliminationBracketRepository extends JpaRepository<EliminationBracket, Long> {
    List<EliminationBracket> findByTournamentId(Long tournamentId);
    List<EliminationBracket> findByTournamentIdAndRound(Long tournamentId, String round);
}
