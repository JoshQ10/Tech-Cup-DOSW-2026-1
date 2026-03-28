package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long> {
    List<Sanction> findByPlayerId(Long playerId);
    List<Sanction> findByTournamentId(Long tournamentId);
    List<Sanction> findByMatchId(Long matchId);
}
