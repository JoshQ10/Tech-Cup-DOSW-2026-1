package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Tournament;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_.TournamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;

    @Override
    public TournamentResponse create(TournamentRequest request) {
        log.info("Creating new tournament: {}", request.getName());

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .teamCount(request.getTeamCount())
                .costPerTeam(request.getCostPerTeam())
                .status(TournamentStatus.DRAFT)
                .build();

        Tournament savedTournament = tournamentRepository.save(tournament);
        log.info("Tournament created successfully with id: {}", savedTournament.getId());

        return mapToTournamentResponse(savedTournament);
    }

    @Override
    public TournamentResponse getById(Long id) {
        log.info("Fetching tournament: {}", id);

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tournament {} not found", id);
                    return new ResourceNotFoundException("Tournament not found");
                });

        return mapToTournamentResponse(tournament);
    }

    @Override
    public TournamentResponse changeStatus(Long id, ChangeStatusRequest request) {
        log.info("Changing status for tournament: {} to {}", id, request.getStatus());

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tournament {} not found", id);
                    return new ResourceNotFoundException("Tournament not found");
                });

        tournament.setStatus(request.getStatus());

        Tournament updatedTournament = tournamentRepository.save(tournament);
        log.info("Tournament status updated successfully for id: {}", id);

        return mapToTournamentResponse(updatedTournament);
    }

    private TournamentResponse mapToTournamentResponse(Tournament tournament) {
        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .teamCount(tournament.getTeamCount())
                .costPerTeam(tournament.getCostPerTeam())
                .status(tournament.getStatus())
                .build();
    }
}
