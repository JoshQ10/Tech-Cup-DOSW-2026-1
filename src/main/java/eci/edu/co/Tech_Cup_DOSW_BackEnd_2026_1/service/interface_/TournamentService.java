package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TournamentResponse;

public interface TournamentService {
    TournamentResponse create(TournamentRequest request);

    TournamentResponse getById(String id);

    TournamentResponse configure(String id, TournamentConfigRequest request);

    TournamentResponse changeStatus(String id, ChangeStatusRequest request);
}
