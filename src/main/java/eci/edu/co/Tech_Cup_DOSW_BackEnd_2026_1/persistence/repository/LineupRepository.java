package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineupRepository extends JpaRepository<LineupEntity, Long> {
    Optional<LineupEntity> findByMatchIdAndTeamId(Long matchId, Long teamId);
}
