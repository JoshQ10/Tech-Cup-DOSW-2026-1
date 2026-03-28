package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TeamResponse;

public interface TeamService {
    TeamResponse create(TeamRequest request);

    TeamResponse getById(String id);

    TeamResponse getRoster(String id);

    TeamResponse removePlayer(String teamId, String playerId);
}
