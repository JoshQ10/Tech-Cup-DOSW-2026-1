package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentRulesConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentRulesConfirmationRepository extends JpaRepository<TournamentRulesConfirmationEntity, Long> {

    Optional<TournamentRulesConfirmationEntity> findByTournamentIdAndUserId(Long tournamentId, Long userId);
}
