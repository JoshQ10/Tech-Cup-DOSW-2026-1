package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<StandingEntity, Long> {
    List<StandingEntity> findByTournamentIdOrderByPointsDescGoalDifferenceDesc(Long tournamentId);

    List<StandingEntity> findByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(Long tournamentId);

    Optional<StandingEntity> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
