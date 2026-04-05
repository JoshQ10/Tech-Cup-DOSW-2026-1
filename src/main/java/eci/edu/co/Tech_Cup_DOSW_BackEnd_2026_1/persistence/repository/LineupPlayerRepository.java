package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.LineupPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineupPlayerRepository extends JpaRepository<LineupPlayer, Long> {
    List<LineupPlayer> findByLineupId(Long lineupId);
    List<LineupPlayer> findByLineupIdAndStarter(Long lineupId, boolean starter);
}
