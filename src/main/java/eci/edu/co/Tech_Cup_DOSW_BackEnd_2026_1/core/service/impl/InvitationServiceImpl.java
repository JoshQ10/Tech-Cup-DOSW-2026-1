package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationDetailResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationPageResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.InvitationService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final TeamInvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public InvitationPageResponse listPendingByUser(Long userId, int page, int limit) {
        log.info("Listing pending invitations for user: {}, page: {}, limit: {}", userId, page, limit);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("sentAt").descending());
        Page<TeamInvitationEntity> invitationPage =
                invitationRepository.findByPlayerIdAndStatus(userId, InvitationStatus.PENDING, pageable);

        List<InvitationDetailResponse> items = invitationPage.getContent().stream()
                .map(this::toDetailResponse)
                .toList();

        return InvitationPageResponse.builder()
                .invitations(items)
                .currentPage(invitationPage.getNumber())
                .totalElements(invitationPage.getTotalElements())
                .totalPages(invitationPage.getTotalPages())
                .pageSize(invitationPage.getSize())
                .hasNextPage(invitationPage.hasNext())
                .isFirstPage(invitationPage.isFirst())
                .isLastPage(invitationPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void acceptInvitation(Long invitationId, Long currentUserId) {
        log.info("Player {} accepting invitation {}", currentUserId, invitationId);

        TeamInvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BusinessRuleException("La invitación ya fue procesada");
        }

        if (invitation.getPlayer() == null || !invitation.getPlayer().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Solo puedes aceptar tus propias invitaciones");
        }

        teamRepository.findCurrentTeamByPlayerId(currentUserId).ifPresent(t -> {
            throw new BusinessRuleException("Ya perteneces a un equipo activo");
        });

        TeamEntity team = invitation.getTeam();
        int currentCount = team.getPlayers() != null ? team.getPlayers().size() : 0;
        if (currentCount >= AppConstants.MAX_TEAM_PLAYERS) {
            throw new BusinessRuleException("El equipo no tiene cupo disponible");
        }

        List<Long> players = new ArrayList<>(team.getPlayers() != null ? team.getPlayers() : new ArrayList<>());
        players.add(currentUserId);
        team.setPlayers(players);
        teamRepository.save(team);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        List<TeamInvitationEntity> otherPending =
                invitationRepository.findByPlayerIdAndStatus(currentUserId, InvitationStatus.PENDING);
        for (TeamInvitationEntity other : otherPending) {
            other.setStatus(InvitationStatus.REJECTED);
            other.setRespondedAt(LocalDateTime.now());
        }
        if (!otherPending.isEmpty()) {
            invitationRepository.saveAll(otherPending);
            log.info("{} invitaciones pendientes canceladas para el jugador {}", otherPending.size(), currentUserId);
        }

        log.info("Invitation {} accepted by player {}. Added to team {}", invitationId, currentUserId, team.getId());
    }

    @Override
    @Transactional
    public void rejectInvitation(Long invitationId, Long currentUserId) {
        log.info("Player {} rejecting invitation {}", currentUserId, invitationId);

        TeamInvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BusinessRuleException("La invitación ya fue procesada");
        }

        if (invitation.getPlayer() == null || !invitation.getPlayer().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Solo puedes rechazar tus propias invitaciones");
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setRespondedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        log.info("Invitation {} rejected by player {}", invitationId, currentUserId);
    }

    @Override
    @Transactional
    public void sendInvitation(Long teamId, Long playerId) {
        log.info("Sending invitation to player {} for team {}", playerId, teamId);

        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND));

        int currentCount = team.getPlayers() != null ? team.getPlayers().size() : 0;
        if (currentCount >= AppConstants.MAX_TEAM_PLAYERS) {
            throw new BusinessRuleException("El equipo no tiene cupo disponible para más jugadores");
        }

        UserEntity player = userRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        teamRepository.findCurrentTeamByPlayerId(playerId).ifPresent(t -> {
            throw new BusinessRuleException("El jugador ya pertenece a un equipo activo");
        });

        invitationRepository.findByTeamIdAndPlayerId(teamId, playerId).ifPresent(existing -> {
            if (existing.getStatus() == InvitationStatus.PENDING) {
                throw new BusinessRuleException("Ya existe una invitación pendiente para este jugador en este equipo");
            }
        });

        TeamInvitationEntity invitation = TeamInvitationEntity.builder()
                .team(team)
                .player(player)
                .status(InvitationStatus.PENDING)
                .sentAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);
        log.info("Invitation sent to player {} for team {}", playerId, teamId);
    }

    private InvitationDetailResponse toDetailResponse(TeamInvitationEntity invitation) {
        TeamEntity team = invitation.getTeam();
        String captainName = null;
        if (team != null && team.getCaptainId() != null) {
            captainName = userRepository.findById(team.getCaptainId())
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .orElse(null);
        }

        return InvitationDetailResponse.builder()
                .id(invitation.getId())
                .teamId(team != null ? team.getId() : null)
                .teamName(team != null ? team.getName() : null)
                .teamShieldUrl(team != null ? team.getShieldUrl() : null)
                .captainId(team != null ? team.getCaptainId() : null)
                .captainName(captainName)
                .playersEnrolled(team != null && team.getPlayers() != null ? team.getPlayers().size() : 0)
                .totalCapacity(AppConstants.MAX_TEAM_PLAYERS)
                .sentAt(invitation.getSentAt())
                .status(invitation.getStatus())
                .build();
    }
}
