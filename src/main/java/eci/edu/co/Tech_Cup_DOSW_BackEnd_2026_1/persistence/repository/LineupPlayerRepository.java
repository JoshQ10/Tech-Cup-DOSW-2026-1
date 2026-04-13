package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineupPlayerRepository extends JpaRepository<LineupPlayerEntity, Long> {
    List<LineupPlayerEntity> findByLineupId(Long lineupId);

    List<LineupPlayerEntity> findByLineupIdAndStarter(Long lineupId, boolean starter);

    @Query("SELECT COUNT(DISTINCT lp.lineup.match.id) FROM LineupPlayerEntity lp WHERE lp.player.id = :playerId AND lp.lineup.match.played = true")
    long countPlayedMatchesByPlayerId(@Param("playerId") Long playerId);
}
