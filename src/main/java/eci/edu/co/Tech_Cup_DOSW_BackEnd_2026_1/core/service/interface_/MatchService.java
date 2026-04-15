package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchCardRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchGoalRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchPossessionRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchResultRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchDetailResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchPageResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchPossessionResponse;

public interface MatchService {

    MatchPageResponse getTeamMatches(Long teamId, int page, int limit);

    MatchDetailResponse getMatchDetail(Long matchId);

    MatchDetailResponse registerResult(Long matchId, MatchResultRequest request);

    MatchDetailResponse registerGoals(Long matchId, MatchGoalRequest request);

    MatchDetailResponse registerCards(Long matchId, MatchCardRequest request);

    MatchDetailResponse updateStatus(Long matchId, MatchStatusRequest request);

    MatchPossessionResponse registerPossession(Long matchId, MatchPossessionRequest request);
}
