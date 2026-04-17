package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchCardRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchGoalRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchPossessionRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchResultRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchDetailResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchGoalEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchPageResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchPossessionResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchSummaryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.MatchService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.HeatmapPoint;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchPossessionEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.SanctionEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchPossessionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SanctionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final MatchPossessionRepository matchPossessionRepository;
    private final SanctionRepository sanctionRepository;
    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public MatchPageResponse getTeamMatches(Long teamId, int page, int limit) {
        log.info("Fetching matches for team: {}, page: {}, limit: {}", teamId, page, limit);

        teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND));

        PageRequest pageable = PageRequest.of(page, limit,
                Sort.by("matchDate").descending().and(Sort.by("matchTime").descending()));
        Page<MatchEntity> matchPage = matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId, pageable);

        List<MatchSummaryResponse> matches = matchPage.getContent().stream()
                .map(this::toSummaryResponse)
                .toList();

        return MatchPageResponse.builder()
                .matches(matches)
                .currentPage(matchPage.getNumber())
                .totalElements(matchPage.getTotalElements())
                .totalPages(matchPage.getTotalPages())
                .pageSize(matchPage.getSize())
                .hasNextPage(matchPage.hasNext())
                .isFirstPage(matchPage.isFirst())
                .isLastPage(matchPage.isLast())
                .build();
    }

    @Override
    public MatchDetailResponse getMatchDetail(Long matchId) {
        log.info("Fetching match detail for: {}", matchId);
        MatchEntity match = findMatch(matchId);
        return toDetailResponse(match);
    }

    @Override
    @Transactional
    public MatchDetailResponse registerResult(Long matchId, MatchResultRequest request) {
        log.info("Registering result for match: {} — {}:{}", matchId, request.getHomeScore(), request.getAwayScore());
        MatchEntity match = findMatch(matchId);

        if (match.isPlayed()) {
            throw new BusinessRuleException("El resultado de este partido ya fue registrado");
        }

        match.setHomeScore(request.getHomeScore());
        match.setAwayScore(request.getAwayScore());
        match.setPlayed(true);
        match.setStatus(MatchStatus.FINISHED);

        updateStandings(match, request.getHomeScore(), request.getAwayScore());

        MatchEntity saved = matchRepository.save(match);
        log.info("Result registered for match: {}", matchId);
        return toDetailResponse(saved);
    }

    @Override
    @Transactional
    public MatchDetailResponse registerGoals(Long matchId, MatchGoalRequest request) {
        log.info("Registering {} goal(s) for match: {}", request.getGoals().size(), matchId);
        MatchEntity match = findMatch(matchId);

        for (MatchGoalRequest.GoalEntry entry : request.getGoals()) {
            UserEntity player = userRepository.findById(entry.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
            TeamEntity team = teamRepository.findById(entry.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND));

            MatchEventEntity event = MatchEventEntity.builder()
                    .match(match)
                    .player(player)
                    .team(team)
                    .eventType(MatchEventType.GOAL)
                    .minute(entry.getMinute())
                    .additionalMinutes(entry.getAdditionalMinutes())
                    .build();
            matchEventRepository.save(event);
        }

        log.info("Goals registered for match: {}", matchId);
        return toDetailResponse(match);
    }

    @Override
    @Transactional
    public MatchDetailResponse registerCards(Long matchId, MatchCardRequest request) {
        log.info("Registering {} card(s) for match: {}", request.getCards().size(), matchId);
        MatchEntity match = findMatch(matchId);

        for (MatchCardRequest.CardEntry entry : request.getCards()) {
            if (entry.getCardType() == MatchEventType.GOAL) {
                throw new BusinessRuleException("Solo se permiten tarjetas amarillas o rojas en este endpoint");
            }

            UserEntity player = userRepository.findById(entry.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
            TeamEntity team = teamRepository.findById(entry.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_TEAM_NOT_FOUND));

            MatchEventEntity event = MatchEventEntity.builder()
                    .match(match)
                    .player(player)
                    .team(team)
                    .eventType(entry.getCardType())
                    .minute(entry.getMinute())
                    .additionalMinutes(entry.getAdditionalMinutes())
                    .build();
            matchEventRepository.save(event);

            accumulateSanction(match, player, entry.getCardType());
        }

        log.info("Cards registered for match: {}", matchId);
        return toDetailResponse(match);
    }

    @Override
    @Transactional
    public MatchDetailResponse updateStatus(Long matchId, MatchStatusRequest request) {
        log.info("Updating status for match: {} to {}", matchId, request.getStatus());
        MatchEntity match = findMatch(matchId);

        if (request.getStatus() == MatchStatus.CANCELLED) {
            match.setHomeScore(null);
            match.setAwayScore(null);
            match.setPlayed(false);
        } else if (request.getStatus() == MatchStatus.FINISHED) {
            match.setPlayed(true);
        }

        match.setStatus(request.getStatus());
        MatchEntity saved = matchRepository.save(match);
        log.info("Status updated for match: {}", matchId);
        return toDetailResponse(saved);
    }

    @Override
    @Transactional
    public MatchPossessionResponse registerPossession(Long matchId, MatchPossessionRequest request) {
        log.info("Registering possession data for match: {}", matchId);
        MatchEntity match = findMatch(matchId);

        MatchPossessionEntity possession = matchPossessionRepository.findByMatchId(matchId)
                .orElse(MatchPossessionEntity.builder().match(match).build());

        possession.setHomePercentage(request.getHomePercentage());
        possession.setAwayPercentage(request.getAwayPercentage());

        List<HeatmapPoint> points = new ArrayList<>();
        if (request.getHeatmapPoints() != null) {
            for (MatchPossessionRequest.HeatmapPointDto dto : request.getHeatmapPoints()) {
                points.add(new HeatmapPoint(dto.getX(), dto.getY()));
            }
        }
        possession.setHeatmapPoints(points);

        MatchPossessionEntity saved = matchPossessionRepository.save(possession);
        log.info("Possession registered for match: {}", matchId);
        return toPossessionResponse(saved);
    }

    // ── Private helpers ──────────────────────────────────────────────────────────

    private MatchEntity findMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_MATCH_NOT_FOUND));
    }

    private void updateStandings(MatchEntity match, int homeScore, int awayScore) {
        if (match.getTournament() == null || match.getHomeTeam() == null || match.getAwayTeam() == null) {
            log.warn("Cannot update standings: match {} is missing tournament or team data", match.getId());
            return;
        }

        TournamentEntity tournament = match.getTournament();
        StandingEntity homeStanding = findOrCreateStanding(tournament, match.getHomeTeam());
        StandingEntity awayStanding = findOrCreateStanding(tournament, match.getAwayTeam());

        homeStanding.setPlayed(homeStanding.getPlayed() + 1);
        awayStanding.setPlayed(awayStanding.getPlayed() + 1);

        homeStanding.setGoalsFor(homeStanding.getGoalsFor() + homeScore);
        homeStanding.setGoalsAgainst(homeStanding.getGoalsAgainst() + awayScore);
        awayStanding.setGoalsFor(awayStanding.getGoalsFor() + awayScore);
        awayStanding.setGoalsAgainst(awayStanding.getGoalsAgainst() + homeScore);

        if (homeScore > awayScore) {
            homeStanding.setWon(homeStanding.getWon() + 1);
            homeStanding.setPoints(homeStanding.getPoints() + 3);
            awayStanding.setLost(awayStanding.getLost() + 1);
        } else if (awayScore > homeScore) {
            awayStanding.setWon(awayStanding.getWon() + 1);
            awayStanding.setPoints(awayStanding.getPoints() + 3);
            homeStanding.setLost(homeStanding.getLost() + 1);
        } else {
            homeStanding.setDrawn(homeStanding.getDrawn() + 1);
            homeStanding.setPoints(homeStanding.getPoints() + 1);
            awayStanding.setDrawn(awayStanding.getDrawn() + 1);
            awayStanding.setPoints(awayStanding.getPoints() + 1);
        }

        homeStanding.setGoalDifference(homeStanding.getGoalsFor() - homeStanding.getGoalsAgainst());
        awayStanding.setGoalDifference(awayStanding.getGoalsFor() - awayStanding.getGoalsAgainst());

        standingRepository.save(homeStanding);
        standingRepository.save(awayStanding);
    }

    private StandingEntity findOrCreateStanding(TournamentEntity tournament, TeamEntity team) {
        return standingRepository.findByTournamentIdAndTeamId(tournament.getId(), team.getId())
                .orElse(StandingEntity.builder()
                        .tournament(tournament)
                        .team(team)
                        .build());
    }

    private void accumulateSanction(MatchEntity match, UserEntity player, MatchEventType cardType) {
        if (cardType == MatchEventType.RED_CARD) {
            SanctionEntity sanction = SanctionEntity.builder()
                    .tournament(match.getTournament())
                    .player(player)
                    .match(match)
                    .description("Tarjeta roja")
                    .sanctionDate(LocalDate.now())
                    .suspendedMatches(1)
                    .build();
            sanctionRepository.save(sanction);
            return;
        }

        if (cardType == MatchEventType.YELLOW_CARD && match.getTournament() != null) {
            long yellowCount = matchEventRepository.countByPlayerIdAndEventType(
                    player.getId(), MatchEventType.YELLOW_CARD);
            if (yellowCount > 0 && yellowCount % 3 == 0) {
                SanctionEntity sanction = SanctionEntity.builder()
                        .tournament(match.getTournament())
                        .player(player)
                        .match(match)
                        .description("Acumulación de " + yellowCount + " tarjetas amarillas")
                        .sanctionDate(LocalDate.now())
                        .suspendedMatches(1)
                        .build();
                sanctionRepository.save(sanction);
            }
        }
    }

    private MatchSummaryResponse toSummaryResponse(MatchEntity match) {
        return MatchSummaryResponse.builder()
                .id(match.getId())
                .status(match.getStatus())
                .homeTeamId(match.getHomeTeam() != null ? match.getHomeTeam().getId() : null)
                .homeTeamName(match.getHomeTeam() != null ? match.getHomeTeam().getName() : null)
                .awayTeamId(match.getAwayTeam() != null ? match.getAwayTeam().getId() : null)
                .awayTeamName(match.getAwayTeam() != null ? match.getAwayTeam().getName() : null)
                .courtId(match.getCourt() != null ? match.getCourt().getId() : null)
                .courtName(match.getCourt() != null ? match.getCourt().getName() : null)
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .result(formatResult(match))
                .build();
    }

    private MatchDetailResponse toDetailResponse(MatchEntity match) {
        List<MatchEventEntity> events = matchEventRepository.findByMatchId(match.getId());

        List<MatchGoalEventResponse> goals = events.stream()
                .filter(e -> e.getEventType() == MatchEventType.GOAL)
                .map(this::toGoalResponse)
                .toList();

        List<MatchCardEventResponse> cards = events.stream()
                .filter(e -> e.getEventType() == MatchEventType.YELLOW_CARD
                        || e.getEventType() == MatchEventType.RED_CARD)
                .map(this::toCardResponse)
                .toList();

        MatchPossessionResponse possession = matchPossessionRepository.findByMatchId(match.getId())
                .map(this::toPossessionResponse)
                .orElse(null);

        return MatchDetailResponse.builder()
                .id(match.getId())
                .status(match.getStatus())
                .tournamentId(match.getTournament() != null ? match.getTournament().getId() : null)
                .homeTeamId(match.getHomeTeam() != null ? match.getHomeTeam().getId() : null)
                .homeTeamName(match.getHomeTeam() != null ? match.getHomeTeam().getName() : null)
                .awayTeamId(match.getAwayTeam() != null ? match.getAwayTeam().getId() : null)
                .awayTeamName(match.getAwayTeam() != null ? match.getAwayTeam().getName() : null)
                .courtId(match.getCourt() != null ? match.getCourt().getId() : null)
                .courtName(match.getCourt() != null ? match.getCourt().getName() : null)
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .phase(match.getPhase())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .result(formatResult(match))
                .goals(goals)
                .cards(cards)
                .possession(possession)
                .build();
    }

    private MatchGoalEventResponse toGoalResponse(MatchEventEntity event) {
        return MatchGoalEventResponse.builder()
                .playerId(event.getPlayer() != null ? event.getPlayer().getId() : null)
                .playerName(resolvePlayerName(event.getPlayer()))
                .teamId(event.getTeam() != null ? event.getTeam().getId() : null)
                .teamName(event.getTeam() != null ? event.getTeam().getName() : null)
                .minute(formatMinute(event.getMinute(), event.getAdditionalMinutes()))
                .build();
    }

    private MatchCardEventResponse toCardResponse(MatchEventEntity event) {
        return MatchCardEventResponse.builder()
                .cardType(event.getEventType())
                .playerId(event.getPlayer() != null ? event.getPlayer().getId() : null)
                .playerName(resolvePlayerName(event.getPlayer()))
                .teamId(event.getTeam() != null ? event.getTeam().getId() : null)
                .teamName(event.getTeam() != null ? event.getTeam().getName() : null)
                .minute(formatMinute(event.getMinute(), event.getAdditionalMinutes()))
                .build();
    }

    private MatchPossessionResponse toPossessionResponse(MatchPossessionEntity entity) {
        List<MatchPossessionResponse.HeatmapPointResponse> points = entity.getHeatmapPoints() == null
                ? List.of()
                : entity.getHeatmapPoints().stream()
                        .map(p -> MatchPossessionResponse.HeatmapPointResponse.builder()
                                .x(p.getX())
                                .y(p.getY())
                                .build())
                        .toList();

        return MatchPossessionResponse.builder()
                .homePercentage(entity.getHomePercentage())
                .awayPercentage(entity.getAwayPercentage())
                .heatmapPoints(points)
                .build();
    }

    private String formatResult(MatchEntity match) {
        if (match.getStatus() == MatchStatus.CANCELLED) {
            return "X-X";
        }
        if (match.getHomeScore() != null && match.getAwayScore() != null) {
            return match.getHomeScore() + "-" + match.getAwayScore();
        }
        return null;
    }

    private String formatMinute(int minute, int additionalMinutes) {
        return additionalMinutes > 0 ? minute + "+" + additionalMinutes : String.valueOf(minute);
    }

    private String resolvePlayerName(UserEntity player) {
        if (player == null) return null;
        return player.getFirstName() + " " + player.getLastName();
    }
}
