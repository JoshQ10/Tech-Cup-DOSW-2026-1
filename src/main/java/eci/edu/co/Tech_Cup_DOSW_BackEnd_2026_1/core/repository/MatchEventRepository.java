package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.MatchEvent;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByMatchId(Long matchId);
    List<MatchEvent> findByPlayerIdAndEventType(Long playerId, MatchEventType eventType);
    List<MatchEvent> findByMatchIdAndEventType(Long matchId, MatchEventType eventType);
}
