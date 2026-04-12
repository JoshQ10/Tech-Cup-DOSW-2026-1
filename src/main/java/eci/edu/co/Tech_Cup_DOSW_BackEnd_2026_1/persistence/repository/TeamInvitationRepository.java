package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitationEntity, Long> {
    List<TeamInvitationEntity> findByTeamId(Long teamId);
    List<TeamInvitationEntity> findByPlayerId(Long playerId);
    Optional<TeamInvitationEntity> findByTeamIdAndPlayerId(Long teamId, Long playerId);
    List<TeamInvitationEntity> findByPlayerIdAndStatus(Long playerId, InvitationStatus status);
}
