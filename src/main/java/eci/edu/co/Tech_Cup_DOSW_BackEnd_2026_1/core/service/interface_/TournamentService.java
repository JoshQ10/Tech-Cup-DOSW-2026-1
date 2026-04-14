package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ActiveTournamentInfoResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentBracketResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentRulesConfirmationResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentStandingRowResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTopScorerResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMatchHistoryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMonthlyPerformanceResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;

import java.util.List;

public interface TournamentService {
    TournamentResponse create(TournamentRequest request);

    TournamentResponse getById(Long id);

    TournamentResponse configure(Long id, TournamentConfigRequest request);

    TournamentResponse changeStatus(Long id, ChangeStatusRequest request);

    TournamentSetupResponse setup(Long id, TournamentSetupRequest request);

    ActiveTournamentInfoResponse getActiveTournamentInfo();

    List<TournamentStandingRowResponse> getTournamentStandings(Long tournamentId);

    TournamentBracketResponse getTournamentBracket(Long tournamentId);

    List<TournamentCardEventResponse> getTournamentCards(Long tournamentId);

    List<TournamentTopScorerResponse> getTournamentTopScorers(Long tournamentId);

    List<TournamentMatchHistoryResponse> getTournamentMatchHistory(Long tournamentId);

    List<TournamentMonthlyPerformanceResponse> getTournamentMonthlyPerformance(Long tournamentId);

    TournamentRulesConfirmationResponse confirmTournamentRules(Long tournamentId, String authenticatedUser);

    void delete(Long id);
}
