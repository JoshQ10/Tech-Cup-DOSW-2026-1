package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {
    List<MatchEventEntity> findByMatchId(Long matchId);

    List<MatchEventEntity> findByPlayerIdAndEventType(Long playerId, MatchEventType eventType);

    List<MatchEventEntity> findByMatchIdAndEventType(Long matchId, MatchEventType eventType);

    @Query("""
            SELECT e FROM MatchEventEntity e
            WHERE e.match.tournament.id = :tournamentId
              AND e.eventType IN :eventTypes
            ORDER BY e.match.matchDate ASC, e.match.matchTime ASC, e.minute ASC
            """)
    List<MatchEventEntity> findTournamentEventsByTypesOrderByChronology(
            @Param("tournamentId") Long tournamentId,
            @Param("eventTypes") Collection<MatchEventType> eventTypes);

    long countByPlayerIdAndEventType(Long playerId, MatchEventType eventType);
}
