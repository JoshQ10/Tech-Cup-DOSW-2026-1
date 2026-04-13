package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TeamRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TeamPersistenceMapper;
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
    private final TeamRequestValidator teamRequestValidator;
    private final TeamPersistenceMapper teamPersistenceMapper;

    @Override
    public TeamResponse create(TeamRequest request) {
        teamRequestValidator.validate(request);
        log.info("Creating new team: {}", request.getName());

        Team team = Team.builder()
                .name(request.getName())
                .shieldUrl(request.getShieldUrl())
                .uniformColors(request.getUniformColors())
                .tournamentId(request.getTournamentId())
                .captainId(request.getCaptainId())
                .players(new ArrayList<>())
                .build();

        @SuppressWarnings("null")
        Team savedTeam = teamPersistenceMapper.toModel(
                teamRepository.save(teamPersistenceMapper.toEntity(team)));
        log.info("Team created successfully with id: {}", savedTeam.getId());

        return mapToTeamResponse(savedTeam);
    }

    @Override
    public TeamResponse update(Long id, TeamRequest request) {
        teamRequestValidator.validate(request);
        log.info("Updating team: {}", id);

        Team team = teamRepository.findById(id)
                .map(teamPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });

        team.setName(request.getName());
        team.setShieldUrl(request.getShieldUrl());
        team.setUniformColors(request.getUniformColors());
        team.setTournamentId(request.getTournamentId());
        team.setCaptainId(request.getCaptainId());

        Team updatedTeam = teamPersistenceMapper.toModel(
                teamRepository.save(teamPersistenceMapper.toEntity(team)));
        log.info("Team updated successfully: {}", id);

        return mapToTeamResponse(updatedTeam);
    }

    @Override
    public TeamResponse getById(Long id) {
        log.info("Fetching team: {}", id);

        @SuppressWarnings("null")
        Team team = teamRepository.findById(id)
                .map(teamPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });

        return mapToTeamResponse(team);
    }

    @Override
    public TeamResponse getRoster(Long id) {
        log.info("Fetching roster for team: {}", id);
        @SuppressWarnings("null")
        Team team = teamRepository.findById(id)
                .map(teamPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Team {} not found for roster", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });
        return mapToTeamResponse(team);
    }

    @Override
    public TeamResponse removePlayer(Long teamId, Long playerId) {
        log.info("Removing player: {} from team: {}", playerId, teamId);

        @SuppressWarnings("null")
        Team team = teamRepository.findById(teamId)
                .map(teamPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", teamId);
                    return new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
                });

        if (team.getPlayers() == null || !team.getPlayers().contains(playerId)) {
            log.warn("Player {} not found in team {}", playerId, teamId);
            throw new BusinessRuleException("Player not found in this team");
        }

        team.getPlayers().remove(playerId);

        Team updatedTeam = teamPersistenceMapper.toModel(
                teamRepository.save(teamPersistenceMapper.toEntity(team)));
        log.info("Player removed successfully from team: {}", teamId);

        return mapToTeamResponse(updatedTeam);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting team: {}", id);

        if (!teamRepository.existsById(id)) {
            log.warn("Team {} not found for deletion", id);
            throw new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND);
        }

        try {
            teamRepository.deleteById(id);
            log.info("Team deleted successfully: {}", id);
        } catch (RuntimeException ex) {
            log.warn("Could not delete team {} due to linked data", id);
            throw new BusinessRuleException("No se puede eliminar el equipo porque tiene datos asociados");
        }
    }

    private TeamResponse mapToTeamResponse(Team team) {
        return teamMapper.toResponse(team);
    }
}
