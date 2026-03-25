package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Team;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Tournament;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.User;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    @Override
    public TeamResponse create(TeamRequest request) {
        log.info("Creating new team: {}", request.getName());

        Tournament tournament = tournamentRepository.findById(request.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        User captain = userRepository.findById(request.getCaptainId())
                .orElseThrow(() -> new ResourceNotFoundException("Captain not found"));

        Team team = Team.builder()
                .name(request.getName())
                .shieldUrl(request.getShieldUrl())
                .uniformColors(request.getUniformColors())
                .tournament(tournament)
                .captain(captain)
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
                    return new ResourceNotFoundException("Team not found");
                });

        return mapToTeamResponse(team);
    }

    @Override
    public TeamResponse removePlayer(Long teamId, Long playerId) {
        log.info("Removing player: {} from team: {}", playerId, teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.warn("Team {} not found", teamId);
                    return new ResourceNotFoundException("Team not found");
                });

        boolean removed = team.getPlayers().removeIf(player -> player.getId().equals(playerId));
        if (!removed) {
            log.warn("Player {} not found in team {}", playerId, teamId);
            throw new BusinessRuleException("Player not found in this team");
        }

        Team updatedTeam = teamRepository.save(team);
        log.info("Player removed successfully from team: {}", teamId);

        return mapToTeamResponse(updatedTeam);
    }

    private TeamResponse mapToTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .shieldUrl(team.getShieldUrl())
                .uniformColors(team.getUniformColors())
                .tournamentId(team.getTournament() != null ? team.getTournament().getId() : null)
                .captainId(team.getCaptain() != null ? team.getCaptain().getId() : null)
                .players(team.getPlayers() != null
                        ? team.getPlayers().stream().map(User::getId).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }
}
