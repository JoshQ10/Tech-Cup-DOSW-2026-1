package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public TeamResponse create(TeamRequest request) {
        log.info("Creating new team: {}", request.getName());

        Team team = Team.builder()
                .name(request.getName())
                .shieldUrl(request.getShieldUrl())
                .uniformColors(request.getUniformColors())
                .tournamentId(request.getTournamentId())
                .captainId(request.getCaptainId())
                .players(new ArrayList<>())
                .build();

        Team savedTeam = teamRepository.save(team);
        log.info("Team created successfully with id: {}", savedTeam.getId());

        return mapToTeamResponse(savedTeam);
    }

    @Override
    public TeamResponse getById(Long id) {
        log.info("Fetching team: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });

        return mapToTeamResponse(team);
    }

    @Override
    public TeamResponse getRoster(Long id) {
        log.info("Fetching roster for team: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Team {} not found for roster", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });
        return mapToTeamResponse(team);
    }

    @Override
    public TeamResponse removePlayer(Long teamId, Long playerId) {
        log.info("Removing player: {} from team: {}", playerId, teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", teamId);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });

        if (team.getPlayers() == null || !team.getPlayers().contains(playerId)) {
            log.warn("Player {} not found in team {}", playerId, teamId);
            throw new BusinessRuleException("Player not found in this team");
        }

        team.getPlayers().remove(playerId);

        Team updatedTeam = teamRepository.save(team);
        log.info("Player removed successfully from team: {}", teamId);

        return mapToTeamResponse(updatedTeam);
    }

    private TeamResponse mapToTeamResponse(Team team) {
        return teamMapper.toResponse(team);
    }
}
