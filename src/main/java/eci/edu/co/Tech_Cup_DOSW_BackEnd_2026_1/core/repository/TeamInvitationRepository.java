package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.TeamInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    List<TeamInvitation> findByTeamId(Long teamId);
    List<TeamInvitation> findByPlayerId(Long playerId);
    Optional<TeamInvitation> findByTeamIdAndPlayerId(Long teamId, Long playerId);
    List<TeamInvitation> findByPlayerIdAndStatus(Long playerId, InvitationStatus status);
}
