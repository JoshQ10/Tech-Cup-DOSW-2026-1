package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.TournamentResponse;

public interface TournamentService {
    TournamentResponse create(TournamentRequest request);
    TournamentResponse getById(Long id);
    TournamentResponse changeStatus(Long id, ChangeStatusRequest request);
}
