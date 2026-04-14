package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ActiveTournamentInfoResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.CourtResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentBracketResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentRulesConfirmationResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentStandingRowResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTopScorerResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMatchHistoryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMonthlyPerformanceResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentDateResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
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
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.EliminationBracketEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentRulesConfirmationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TournamentPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.EliminationBracketRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRulesConfirmationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class TournamentServiceImpl implements TournamentService {

        private final TournamentRepository tournamentRepository;
        private final CourtRepository courtRepository;
        private final TeamRepository teamRepository;
        private final EliminationBracketRepository eliminationBracketRepository;
        private final MatchEventRepository matchEventRepository;
        private final UserRepository userRepository;
        private final TournamentRulesConfirmationRepository tournamentRulesConfirmationRepository;
        private final MatchRepository matchRepository;
        private final StandingRepository standingRepository;
        private final TournamentDateRepository tournamentDateRepository;
        private final TournamentMapper tournamentMapper;
        private final TournamentRequestValidator tournamentRequestValidator;
        private final TournamentConfigRequestValidator tournamentConfigRequestValidator;
        private final TournamentSetupRequestValidator tournamentSetupRequestValidator;
        private final ChangeStatusRequestValidator changeStatusRequestValidator;
        private final TournamentPersistenceMapper tournamentPersistenceMapper;

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

                Tournament savedTournament = tournamentPersistenceMapper.toModel(
                                tournamentRepository.save(tournamentPersistenceMapper.toEntity(tournament)));
                log.info("Tournament created successfully with id: {}", savedTournament.getId());

                return mapToTournamentResponse(savedTournament);
        }

        @Override
        public TournamentResponse getById(Long id) {
                log.info("Fetching tournament: {}", id);

                Tournament tournament = tournamentRepository.findById(id)
                                .map(tournamentPersistenceMapper::toModel)
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
                                request.getStartDate(), request.getEndDate(), request.getTeamCount(),
                                request.getCostPerTeam());

                Tournament tournament = tournamentRepository.findById(id)
                                .map(tournamentPersistenceMapper::toModel)
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

                Tournament updatedTournament = tournamentPersistenceMapper.toModel(
                                tournamentRepository.save(tournamentPersistenceMapper.toEntity(tournament)));
                log.info("Tournament configured successfully for id: {}", id);
                log.debug("Tournament persisted to database");

                return mapToTournamentResponse(updatedTournament);
        }

        @Override
        public TournamentResponse changeStatus(Long id, ChangeStatusRequest request) {
                log.info("Changing status for tournament: {} to {}", id, request.getStatus());

                Tournament tournament = tournamentRepository.findById(id)
                                .map(tournamentPersistenceMapper::toModel)
                                .orElseThrow(() -> {
                                        log.warn("Tournament {} not found", id);
                                        return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                                });
                log.debug("Tournament state transition - from: {} to: {}", tournament.getStatus(), request.getStatus());

                changeStatusRequestValidator.validate(tournament.getStatus(), request.getStatus());
                log.debug("Status transition validated successfully");

                tournament.setStatus(request.getStatus());
                log.debug("Tournament status updated in memory");

                Tournament updatedTournament = tournamentPersistenceMapper.toModel(
                                tournamentRepository.save(tournamentPersistenceMapper.toEntity(tournament)));
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
                                .map(tournamentPersistenceMapper::toModel)
                                .orElseThrow(() -> {
                                        log.warn("Tournament {} not found", id);
                                        return new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                                });

                tournament.setRules(request.getRules());
                tournament.setSanctionRules(request.getSanctionRules());
                if (request.getInscriptionCloseDate() != null) {
                        tournament.setInscriptionCloseDate(request.getInscriptionCloseDate());
                }
                tournamentRepository.save(tournamentPersistenceMapper.toEntity(tournament));
                log.debug("Tournament rules and sanction config updated");

                courtRepository.deleteAll(courtRepository.findByTournamentId(id));
                List<Court> courts = request.getCourts().stream()
                                .map(cr -> Court.builder()
                                                .name(cr.getName())
                                                .location(cr.getLocation())
                                                .tournament(tournament)
                                                .build())
                                .toList();
                List<Court> savedCourts = courtRepository.saveAll(
                                courts.stream().map(tournamentPersistenceMapper::toEntity).toList())
                                .stream().map(tournamentPersistenceMapper::toModel).toList();
                log.debug("Saved {} courts for tournament {}", savedCourts.size(), id);

                tournamentDateRepository.deleteAll(tournamentDateRepository.findByTournamentId(id));
                List<TournamentDate> dates = request.getSchedule().stream()
                                .map(dr -> TournamentDate.builder()
                                                .description(dr.getDescription())
                                                .eventDate(dr.getEventDate())
                                                .tournament(tournament)
                                                .build())
                                .toList();
                List<TournamentDate> savedDates = tournamentDateRepository.saveAll(
                                dates.stream().map(tournamentPersistenceMapper::toEntity).toList())
                                .stream().map(tournamentPersistenceMapper::toModel).toList();
                log.debug("Saved {} dates for tournament {}", savedDates.size(), id);

                log.info("Tournament {} setup completed successfully", id);
                return buildSetupResponse(tournament, savedCourts, savedDates);
        }

        @Override
        public ActiveTournamentInfoResponse getActiveTournamentInfo() {
                List<TournamentStatus> activeStatuses = Arrays.asList(TournamentStatus.IN_PROGRESS,
                                TournamentStatus.ACTIVE);
                TournamentEntity activeTournament = tournamentRepository.findTopByStatusInOrderByIdDesc(activeStatuses)
                                .orElseThrow(() -> new ResourceNotFoundException("No active tournament found"));

                long registeredTeams = teamRepository.countByTournamentId(activeTournament.getId());
                long availableCourts = courtRepository.countByTournamentId(activeTournament.getId());
                String currentPhase = resolveCurrentPhase(activeTournament.getId());

                return ActiveTournamentInfoResponse.builder()
                                .id(activeTournament.getId())
                                .name(activeTournament.getName())
                                .startDate(activeTournament.getStartDate())
                                .endDate(activeTournament.getEndDate())
                                .status(activeTournament.getStatus())
                                .registeredTeams(registeredTeams)
                                .availableCourts(availableCourts)
                                .currentPhase(currentPhase)
                                .build();
        }

        @Override
        public List<TournamentStandingRowResponse> getTournamentStandings(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                List<StandingEntity> standings = standingRepository
                                .findByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournamentId);

                return java.util.stream.IntStream.range(0, standings.size())
                                .mapToObj(index -> {
                                        StandingEntity standing = standings.get(index);
                                        return TournamentStandingRowResponse.builder()
                                                        .pos(index + 1)
                                                        .teamId(standing.getTeam() != null ? standing.getTeam().getId()
                                                                        : null)
                                                        .team(standing.getTeam() != null ? standing.getTeam().getName()
                                                                        : "N/A")
                                                        .pj(standing.getPlayed())
                                                        .pg(standing.getWon())
                                                        .pe(standing.getDrawn())
                                                        .pp(standing.getLost())
                                                        .gf(standing.getGoalsFor())
                                                        .gc(standing.getGoalsAgainst())
                                                        .dc(standing.getGoalDifference())
                                                        .pts(standing.getPoints())
                                                        .build();
                                })
                                .toList();
        }

        @Override
        public TournamentBracketResponse getTournamentBracket(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                List<EliminationBracketEntity> brackets = eliminationBracketRepository
                                .findByTournamentIdOrderByRoundAscMatchPositionAsc(tournamentId)
                                .stream()
                                .sorted(Comparator
                                                .comparingInt((EliminationBracketEntity b) -> roundOrder(b.getRound()))
                                                .thenComparingInt(EliminationBracketEntity::getMatchPosition))
                                .toList();

                Map<String, List<TournamentBracketResponse.BracketMatchResponse>> matchesByPhase = new LinkedHashMap<>();
                Map<String, LinkedHashSet<String>> teamsByPhase = new LinkedHashMap<>();

                for (EliminationBracketEntity bracket : brackets) {
                        String phase = mapRoundLabel(bracket.getRound());
                        MatchEntity match = bracket.getMatch();

                        Long homeTeamId = match != null && match.getHomeTeam() != null ? match.getHomeTeam().getId()
                                        : null;
                        String homeTeam = match != null && match.getHomeTeam() != null ? match.getHomeTeam().getName()
                                        : "POR CLASIFICAR";
                        Long awayTeamId = match != null && match.getAwayTeam() != null ? match.getAwayTeam().getId()
                                        : null;
                        String awayTeam = match != null && match.getAwayTeam() != null ? match.getAwayTeam().getName()
                                        : "POR CLASIFICAR";

                        TournamentBracketResponse.BracketMatchResponse matchResponse = TournamentBracketResponse.BracketMatchResponse
                                        .builder()
                                        .slot(bracket.getMatchPosition())
                                        .matchId(match != null ? match.getId() : null)
                                        .matchDate(match != null ? match.getMatchDate() : null)
                                        .matchTime(match != null ? match.getMatchTime() : null)
                                        .homeTeamId(homeTeamId)
                                        .homeTeam(homeTeam)
                                        .awayTeamId(awayTeamId)
                                        .awayTeam(awayTeam)
                                        .homeScore(match != null ? match.getHomeScore() : null)
                                        .awayScore(match != null ? match.getAwayScore() : null)
                                        .played(match != null && match.isPlayed())
                                        .build();

                        matchesByPhase.computeIfAbsent(phase, key -> new java.util.ArrayList<>()).add(matchResponse);

                        LinkedHashSet<String> teams = teamsByPhase.computeIfAbsent(phase, key -> new LinkedHashSet<>());
                        if (homeTeamId != null) {
                                teams.add(homeTeam);
                        }
                        if (awayTeamId != null) {
                                teams.add(awayTeam);
                        }
                }

                List<TournamentBracketResponse.BracketPhaseResponse> phases = matchesByPhase.entrySet().stream()
                                .map(entry -> TournamentBracketResponse.BracketPhaseResponse.builder()
                                                .phase(entry.getKey())
                                                .classifiedTeams(List.copyOf(teamsByPhase.getOrDefault(entry.getKey(),
                                                                new LinkedHashSet<>())))
                                                .matches(entry.getValue())
                                                .build())
                                .toList();

                return TournamentBracketResponse.builder()
                                .tournamentId(tournamentId)
                                .phases(phases)
                                .build();
        }

        @Override
        public List<TournamentCardEventResponse> getTournamentCards(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                List<MatchEventEntity> cardEvents = matchEventRepository.findTournamentEventsByTypesOrderByChronology(
                                tournamentId,
                                List.of(MatchEventType.YELLOW_CARD, MatchEventType.RED_CARD));

                return cardEvents.stream()
                                .map(event -> TournamentCardEventResponse.builder()
                                                .type(toCardType(event.getEventType()))
                                                .matchDate(event.getMatch() != null ? event.getMatch().getMatchDate()
                                                                : null)
                                                .matchTime(event.getMatch() != null ? event.getMatch().getMatchTime()
                                                                : null)
                                                .matchId(event.getMatch() != null ? event.getMatch().getId() : null)
                                                .playerId(event.getPlayer() != null ? event.getPlayer().getId() : null)
                                                .player(buildPlayerName(event))
                                                .minute(event.getMinute())
                                                .build())
                                .toList();
        }

        @Override
        public List<TournamentTopScorerResponse> getTournamentTopScorers(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                List<MatchEventEntity> goalEvents = matchEventRepository.findTournamentEventsByTypesOrderByChronology(
                                tournamentId,
                                List.of(MatchEventType.GOAL));

                Map<Long, TournamentTopScorerResponse> scorerByPlayer = new HashMap<>();

                for (MatchEventEntity event : goalEvents) {
                        UserEntity player = event.getPlayer();
                        if (player == null || player.getId() == null) {
                                continue;
                        }

                        TournamentTopScorerResponse scorer = scorerByPlayer.computeIfAbsent(player.getId(), id -> {
                                TeamEntity team = event.getTeam();
                                return TournamentTopScorerResponse.builder()
                                                .playerId(id)
                                                .player(buildUserDisplayName(player))
                                                .teamId(team != null ? team.getId() : null)
                                                .team(team != null ? team.getName() : "N/A")
                                                .playerPhotoUrl(player.getAvatarUrl())
                                                .goals(0)
                                                .build();
                        });

                        scorer.setGoals(scorer.getGoals() + 1);

                        if (scorer.getTeamId() == null && event.getTeam() != null) {
                                scorer.setTeamId(event.getTeam().getId());
                                scorer.setTeam(event.getTeam().getName());
                        }
                }

                return scorerByPlayer.values().stream()
                                .sorted(Comparator.comparingInt(TournamentTopScorerResponse::getGoals)
                                                .reversed()
                                                .thenComparing(
                                                                scorer -> scorer.getPlayer() == null ? ""
                                                                                : scorer.getPlayer(),
                                                                String.CASE_INSENSITIVE_ORDER))
                                .toList();
        }

        @Override
        public List<TournamentMatchHistoryResponse> getTournamentMatchHistory(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                return matchRepository.findByTournamentId(tournamentId).stream()
                                .filter(MatchEntity::isPlayed)
                                .sorted(Comparator
                                                .comparing(MatchEntity::getMatchDate,
                                                                Comparator.nullsLast(Comparator.naturalOrder()))
                                                .thenComparing(MatchEntity::getMatchTime,
                                                                Comparator.nullsLast(Comparator.naturalOrder())))
                                .map(match -> TournamentMatchHistoryResponse.builder()
                                                .matchId(match.getId())
                                                .matchDate(match.getMatchDate())
                                                .matchTime(match.getMatchTime())
                                                .phase(match.getPhase() != null ? match.getPhase().name() : null)
                                                .homeTeamId(match.getHomeTeam() != null ? match.getHomeTeam().getId()
                                                                : null)
                                                .homeTeam(match.getHomeTeam() != null ? match.getHomeTeam().getName()
                                                                : "N/A")
                                                .homeScore(match.getHomeScore())
                                                .awayTeamId(match.getAwayTeam() != null ? match.getAwayTeam().getId()
                                                                : null)
                                                .awayTeam(match.getAwayTeam() != null ? match.getAwayTeam().getName()
                                                                : "N/A")
                                                .awayScore(match.getAwayScore())
                                                .build())
                                .toList();
        }

        @Override
        public List<TournamentMonthlyPerformanceResponse> getTournamentMonthlyPerformance(Long tournamentId) {
                if (!tournamentRepository.existsById(tournamentId)) {
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                List<TeamEntity> teams = teamRepository.findByTournamentId(tournamentId);
                Map<Long, TeamMonthlyAccumulator> accumulatorByTeam = new LinkedHashMap<>();

                for (TeamEntity team : teams) {
                        if (team.getId() != null) {
                                accumulatorByTeam.put(team.getId(), TeamMonthlyAccumulator.fromTeam(team));
                        }
                }

                List<MatchEntity> playedMatches = matchRepository.findByTournamentId(tournamentId).stream()
                                .filter(MatchEntity::isPlayed)
                                .filter(match -> match.getMatchDate() != null)
                                .filter(match -> match.getHomeTeam() != null && match.getAwayTeam() != null)
                                .toList();

                for (MatchEntity match : playedMatches) {
                        int month = match.getMatchDate().getMonthValue();
                        if (month < 1 || month > 4) {
                                continue;
                        }

                        Integer homeScore = match.getHomeScore();
                        Integer awayScore = match.getAwayScore();
                        if (homeScore == null || awayScore == null) {
                                continue;
                        }

                        int homePoints;
                        int awayPoints;
                        boolean homeWin = false;
                        boolean awayWin = false;

                        if (homeScore > awayScore) {
                                homePoints = 3;
                                awayPoints = 0;
                                homeWin = true;
                        } else if (homeScore < awayScore) {
                                homePoints = 0;
                                awayPoints = 3;
                                awayWin = true;
                        } else {
                                homePoints = 1;
                                awayPoints = 1;
                        }

                        updateTeamMonthlyStats(accumulatorByTeam, match.getHomeTeam().getId(), month, homePoints,
                                        homeWin);
                        updateTeamMonthlyStats(accumulatorByTeam, match.getAwayTeam().getId(), month, awayPoints,
                                        awayWin);
                }

                return accumulatorByTeam.values().stream()
                                .map(TeamMonthlyAccumulator::toResponse)
                                .toList();
        }

        private String toCardType(MatchEventType eventType) {
                if (eventType == MatchEventType.YELLOW_CARD) {
                        return "AMARILLA";
                }
                if (eventType == MatchEventType.RED_CARD) {
                        return "ROJA";
                }
                return eventType != null ? eventType.name() : "N/A";
        }

        @Override
        public TournamentRulesConfirmationResponse confirmTournamentRules(Long tournamentId, String authenticatedUser) {
                TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                AppConstants.ERROR_TOURNAMENT_NOT_FOUND));

                UserEntity user = userRepository.findByEmail(authenticatedUser)
                                .or(() -> userRepository.findByUsername(authenticatedUser))
                                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

                TournamentRulesConfirmationEntity confirmation = tournamentRulesConfirmationRepository
                                .findByTournamentIdAndUserId(tournamentId, user.getId())
                                .orElseGet(() -> TournamentRulesConfirmationEntity.builder()
                                                .tournament(tournament)
                                                .user(user)
                                                .build());

                confirmation.setConfirmedAt(LocalDateTime.now());
                TournamentRulesConfirmationEntity saved = tournamentRulesConfirmationRepository.save(confirmation);

                return TournamentRulesConfirmationResponse.builder()
                                .tournamentId(tournamentId)
                                .userId(user.getId())
                                .confirmedAt(saved.getConfirmedAt())
                                .rulesConfirmed(true)
                                .build();
        }

        private String buildPlayerName(MatchEventEntity event) {
                if (event.getPlayer() == null) {
                        return "N/A";
                }
                String firstName = event.getPlayer().getFirstName() != null ? event.getPlayer().getFirstName().trim()
                                : "";
                String lastName = event.getPlayer().getLastName() != null ? event.getPlayer().getLastName().trim() : "";
                String fullName = (firstName + " " + lastName).trim();
                if (!fullName.isBlank()) {
                        return fullName;
                }
                return event.getPlayer().getUsername() != null ? event.getPlayer().getUsername() : "N/A";
        }

        private String buildUserDisplayName(UserEntity user) {
                if (user == null) {
                        return "N/A";
                }
                String firstName = user.getFirstName() != null ? user.getFirstName().trim() : "";
                String lastName = user.getLastName() != null ? user.getLastName().trim() : "";
                String fullName = (firstName + " " + lastName).trim();
                if (!fullName.isBlank()) {
                        return fullName;
                }
                return user.getUsername() != null ? user.getUsername() : "N/A";
        }

        private void updateTeamMonthlyStats(Map<Long, TeamMonthlyAccumulator> accumulatorByTeam,
                        Long teamId,
                        int month,
                        int points,
                        boolean win) {
                if (teamId == null) {
                        return;
                }
                TeamMonthlyAccumulator teamAccumulator = accumulatorByTeam.get(teamId);
                if (teamAccumulator == null) {
                        return;
                }
                teamAccumulator.addResult(month, points, win);
        }

        private static String monthLabel(int month) {
                return switch (month) {
                        case 1 -> "Enero";
                        case 2 -> "Febrero";
                        case 3 -> "Marzo";
                        case 4 -> "Abril";
                        default -> "Mes " + month;
                };
        }

        private static class TeamMonthlyAccumulator {
                private final Long teamId;
                private final String team;
                private final Map<Integer, int[]> monthStats;

                private TeamMonthlyAccumulator(Long teamId, String team, Map<Integer, int[]> monthStats) {
                        this.teamId = teamId;
                        this.team = team;
                        this.monthStats = monthStats;
                }

                private static TeamMonthlyAccumulator fromTeam(TeamEntity team) {
                        Map<Integer, int[]> monthStats = new LinkedHashMap<>();
                        monthStats.put(1, new int[] { 0, 0 });
                        monthStats.put(2, new int[] { 0, 0 });
                        monthStats.put(3, new int[] { 0, 0 });
                        monthStats.put(4, new int[] { 0, 0 });
                        return new TeamMonthlyAccumulator(team.getId(), team.getName(), monthStats);
                }

                private void addResult(int month, int points, boolean win) {
                        int[] values = monthStats.get(month);
                        if (values == null) {
                                return;
                        }
                        values[0] += points;
                        if (win) {
                                values[1] += 1;
                        }
                }

                private TournamentMonthlyPerformanceResponse toResponse() {
                        List<TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint> monthly = monthStats
                                        .entrySet()
                                        .stream()
                                        .map(entry -> TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint
                                                        .builder()
                                                        .month(entry.getKey())
                                                        .monthLabel(monthLabel(entry.getKey()))
                                                        .points(entry.getValue()[0])
                                                        .wins(entry.getValue()[1])
                                                        .build())
                                        .toList();

                        return TournamentMonthlyPerformanceResponse.builder()
                                        .teamId(teamId)
                                        .team(team)
                                        .monthly(monthly)
                                        .build();
                }
        }

        private String mapRoundLabel(String round) {
                if (round == null || round.isBlank()) {
                        return "Sin fase";
                }

                String normalized = round.trim().toUpperCase();
                return switch (normalized) {
                        case "GROUP" -> "Grupos";
                        case "QUARTER_FINAL", "QUARTERFINAL", "CUARTOS" -> "Cuartos";
                        case "SEMI_FINAL", "SEMIFINAL" -> "Semifinal";
                        case "FINAL" -> "Final";
                        default -> round;
                };
        }

        private int roundOrder(String round) {
                if (round == null || round.isBlank()) {
                        return 99;
                }
                String normalized = round.trim().toUpperCase();
                return switch (normalized) {
                        case "GROUP" -> 1;
                        case "QUARTER_FINAL", "QUARTERFINAL", "CUARTOS" -> 2;
                        case "SEMI_FINAL", "SEMIFINAL" -> 3;
                        case "FINAL" -> 4;
                        default -> 98;
                };
        }

        private String resolveCurrentPhase(Long tournamentId) {
                List<MatchEntity> matches = matchRepository.findByTournamentId(tournamentId);
                if (matches.isEmpty()) {
                        return "Grupos";
                }

                boolean hasPendingFinal = matches.stream()
                                .anyMatch(match -> match.getPhase() == MatchPhase.FINAL && !match.isPlayed());
                if (hasPendingFinal) {
                        return "Final";
                }

                boolean hasPendingSemiFinal = matches.stream()
                                .anyMatch(match -> match.getPhase() == MatchPhase.SEMI_FINAL && !match.isPlayed());
                if (hasPendingSemiFinal) {
                        return "Semifinal";
                }

                boolean hasFinal = matches.stream().anyMatch(match -> match.getPhase() == MatchPhase.FINAL);
                if (hasFinal) {
                        return "Final";
                }

                boolean hasSemiFinal = matches.stream().anyMatch(match -> match.getPhase() == MatchPhase.SEMI_FINAL);
                if (hasSemiFinal) {
                        return "Semifinal";
                }

                return "Grupos";
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

        @Override
        public void delete(Long id) {
                log.info("Deleting tournament: {}", id);

                if (!tournamentRepository.existsById(id)) {
                        log.warn("Tournament {} not found for deletion", id);
                        throw new ResourceNotFoundException(AppConstants.ERROR_TOURNAMENT_NOT_FOUND);
                }

                try {
                        tournamentRepository.deleteById(id);
                        log.info("Tournament deleted successfully: {}", id);
                } catch (RuntimeException ex) {
                        log.warn("Could not delete tournament {} due to linked data", id);
                        throw new BusinessRuleException("No se puede eliminar el torneo porque tiene datos asociados");
                }
        }
}
