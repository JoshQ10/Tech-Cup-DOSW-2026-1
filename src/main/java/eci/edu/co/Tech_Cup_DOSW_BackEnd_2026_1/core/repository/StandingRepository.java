package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long> {
    List<Standing> findByTournamentIdOrderByPointsDescGoalDifferenceDesc(Long tournamentId);
    Optional<Standing> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
