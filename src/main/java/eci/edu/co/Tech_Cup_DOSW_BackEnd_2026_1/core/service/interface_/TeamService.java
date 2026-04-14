package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTeamSummaryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserTeamResponse;

import java.util.List;

public interface TeamService {
    TeamResponse create(TeamRequest request);

    TeamResponse update(Long id, TeamRequest request);

    TeamResponse getById(Long id);

    TeamResponse getRoster(Long id);

    TeamResponse removePlayer(Long teamId, Long playerId);

    void delete(Long id);

    List<TournamentTeamSummaryResponse> getTeamsByTournament(Long tournamentId);

    UserTeamResponse getUserTeam(Long userId);
}
