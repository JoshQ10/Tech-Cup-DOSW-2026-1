package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    long countByTournamentId(Long tournamentId);

    List<TeamEntity> findByTournamentId(Long tournamentId);

    @Query("SELECT t FROM TeamEntity t JOIN t.players p WHERE p = :playerId ORDER BY t.id DESC")
    Optional<TeamEntity> findCurrentTeamByPlayerId(@Param("playerId") Long playerId);
}
