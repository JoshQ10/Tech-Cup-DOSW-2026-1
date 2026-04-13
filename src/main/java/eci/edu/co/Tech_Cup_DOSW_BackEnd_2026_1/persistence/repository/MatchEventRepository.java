package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {
    List<MatchEventEntity> findByMatchId(Long matchId);

    List<MatchEventEntity> findByPlayerIdAndEventType(Long playerId, MatchEventType eventType);

    List<MatchEventEntity> findByMatchIdAndEventType(Long matchId, MatchEventType eventType);

    long countByPlayerIdAndEventType(Long playerId, MatchEventType eventType);
}
