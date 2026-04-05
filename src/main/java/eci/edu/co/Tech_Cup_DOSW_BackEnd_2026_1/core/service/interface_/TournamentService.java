package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;

public interface TournamentService {
    TournamentResponse create(TournamentRequest request);

    TournamentResponse getById(Long id);

    TournamentResponse configure(Long id, TournamentConfigRequest request);

    TournamentResponse changeStatus(Long id, ChangeStatusRequest request);

    TournamentSetupResponse setup(Long id, TournamentSetupRequest request);
}
