package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.CourtResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentDateResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Court;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.TournamentDate;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.ChangeStatusRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentConfigRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentSetupRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final CourtRepository courtRepository;
    private final TournamentDateRepository tournamentDateRepository;
    private final TournamentMapper tournamentMapper;
    private final TournamentRequestValidator tournamentRequestValidator;
    private final TournamentConfigRequestValidator tournamentConfigRequestValidator;
    private final TournamentSetupRequestValidator tournamentSetupRequestValidator;
    private final ChangeStatusRequestValidator changeStatusRequestValidator;

    @Override
    public TournamentResponse create(TournamentRequest request) {
        tournamentRequestValidator.validate(request);
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
                    return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                });

        return mapToTournamentResponse(tournament);
    }

    @Override
    public TournamentResponse configure(Long id, TournamentConfigRequest request) {
        tournamentConfigRequestValidator.validate(request);
        log.info("Configuring tournament: {}", id);
        log.debug("Configuration parameters - startDate: {}, endDate: {}, teamCount: {}, costPerTeam: {}",
                request.getStartDate(), request.getEndDate(), request.getTeamCount(), request.getCostPerTeam());

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tournament {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                });
        log.debug("Tournament retrieved with current status: {}", tournament.getStatus());

        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setTeamCount(request.getTeamCount());
        tournament.setCostPerTeam(request.getCostPerTeam());
        log.debug("Tournament configuration updated in memory");

        Tournament updatedTournament = tournamentRepository.save(tournament);
        log.info("Tournament configured successfully for id: {}", id);
        log.debug("Tournament persisted to database");

        return mapToTournamentResponse(updatedTournament);
    }

    @Override
    public TournamentResponse changeStatus(Long id, ChangeStatusRequest request) {
        log.info("Changing status for tournament: {} to {}", id, request.getStatus());

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tournament {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                });
        log.debug("Tournament state transition - from: {} to: {}", tournament.getStatus(), request.getStatus());

        changeStatusRequestValidator.validate(tournament.getStatus(), request.getStatus());
        log.debug("Status transition validated successfully");

        tournament.setStatus(request.getStatus());
        log.debug("Tournament status updated in memory");

        Tournament updatedTournament = tournamentRepository.save(tournament);
        log.info("Tournament status updated successfully for id: {}", id);
        log.debug("Status transition persisted to database");

        return mapToTournamentResponse(updatedTournament);
    }

    @Override
    @Transactional
    public TournamentSetupResponse setup(Long id, TournamentSetupRequest request) {
        tournamentSetupRequestValidator.validate(request);
        log.info("Setting up tournament: {}", id);

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tournament {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                });

        tournament.setRules(request.getRules());
        tournament.setSanctionRules(request.getSanctionRules());
        if (request.getInscriptionCloseDate() != null) {
            tournament.setInscriptionCloseDate(request.getInscriptionCloseDate());
        }
        tournamentRepository.save(tournament);
        log.debug("Tournament rules and sanction config updated");

        courtRepository.deleteAll(courtRepository.findByTournamentId(id));
        List<Court> courts = request.getCourts().stream()
                .map(cr -> Court.builder()
                        .name(cr.getName())
                        .location(cr.getLocation())
                        .tournament(tournament)
                        .build())
                .toList();
        List<Court> savedCourts = courtRepository.saveAll(courts);
        log.debug("Saved {} courts for tournament {}", savedCourts.size(), id);

        tournamentDateRepository.deleteAll(tournamentDateRepository.findByTournamentId(id));
        List<TournamentDate> dates = request.getSchedule().stream()
                .map(dr -> TournamentDate.builder()
                        .description(dr.getDescription())
                        .eventDate(dr.getEventDate())
                        .tournament(tournament)
                        .build())
                .toList();
        List<TournamentDate> savedDates = tournamentDateRepository.saveAll(dates);
        log.debug("Saved {} dates for tournament {}", savedDates.size(), id);

        log.info("Tournament {} setup completed successfully", id);
        return buildSetupResponse(tournament, savedCourts, savedDates);
    }

    private TournamentSetupResponse buildSetupResponse(Tournament tournament,
                                                        List<Court> courts,
                                                        List<TournamentDate> dates) {
        List<CourtResponse> courtResponses = courts.stream()
                .map(c -> CourtResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .location(c.getLocation())
                        .build())
                .toList();

        List<TournamentDateResponse> dateResponses = dates.stream()
                .map(d -> TournamentDateResponse.builder()
                        .id(d.getId())
                        .description(d.getDescription())
                        .eventDate(d.getEventDate())
                        .build())
                .toList();

        return TournamentSetupResponse.builder()
                .tournamentId(tournament.getId())
                .tournamentName(tournament.getName())
                .rules(tournament.getRules())
                .inscriptionCloseDate(tournament.getInscriptionCloseDate())
                .sanctionRules(tournament.getSanctionRules())
                .courts(courtResponses)
                .schedule(dateResponses)
                .build();
    }

    private TournamentResponse mapToTournamentResponse(Tournament tournament) {
        return tournamentMapper.toResponse(tournament);
    }
}
