package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.SanctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanctionRepository extends JpaRepository<SanctionEntity, Long> {
    List<SanctionEntity> findByPlayerId(Long playerId);
    List<SanctionEntity> findByTournamentId(Long tournamentId);
    List<SanctionEntity> findByMatchId(Long matchId);
}
