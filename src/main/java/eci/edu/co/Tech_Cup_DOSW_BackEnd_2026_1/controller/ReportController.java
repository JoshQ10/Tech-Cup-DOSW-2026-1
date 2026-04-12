package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.PaymentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SanctionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Reports and statistics")
public class ReportController {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SanctionRepository sanctionRepository;
    private final StandingRepository standingRepository;

    @GetMapping("/overview")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Overview report", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> overview(@RequestParam(required = false) Long tournamentId) {
        List<MatchEntity> matches = tournamentId == null
                ? matchRepository.findAll()
                : matchRepository.findByTournamentId(tournamentId);

        long playedMatches = matches.stream().filter(MatchEntity::isPlayed).count();
        int totalGoals = matches.stream()
                .mapToInt(m -> (m.getHomeScore() == null ? 0 : m.getHomeScore())
                        + (m.getAwayScore() == null ? 0 : m.getAwayScore()))
                .sum();

        long teams = tournamentId == null
                ? teamRepository.count()
                : teamRepository.findAll().stream().filter(t -> tournamentId.equals(t.getTournamentId())).count();

        List<StandingEntity> standings = tournamentId == null
                ? standingRepository.findAll()
                : standingRepository.findByTournamentIdOrderByPointsDescGoalDifferenceDesc(tournamentId);

        List<Map<String, Object>> topStandings = standings.stream().limit(5).map(s -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("teamId", s.getTeam() != null ? s.getTeam().getId() : null);
            item.put("points", s.getPoints());
            item.put("goalDifference", s.getGoalDifference());
            return item;
        }).toList();

        Map<String, Long> paymentsByStatus = paymentRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getStatus() == null ? "UNKNOWN" : p.getStatus().name(),
                        Collectors.counting()));

        long sanctions = tournamentId == null
                ? sanctionRepository.count()
                : sanctionRepository.findByTournamentId(tournamentId).size();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("tournaments", tournamentId == null ? tournamentRepository.count() : 1);
        response.put("teams", teams);
        response.put("users", userRepository.count());
        response.put("matches", matches.size());
        response.put("playedMatches", playedMatches);
        response.put("pendingMatches", matches.size() - playedMatches);
        response.put("totalGoals", totalGoals);
        response.put("sanctions", sanctions);
        response.put("paymentsByStatus", paymentsByStatus);
        response.put("topStandings", topStandings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/team/{teamId}/summary")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Team summary report", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> teamSummary(@PathVariable Long teamId) {
        List<MatchEntity> teamMatches = matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId);

        long played = teamMatches.stream().filter(MatchEntity::isPlayed).count();
        int goalsFor = teamMatches.stream().mapToInt(m -> {
            if (m.getHomeTeam() != null && teamId.equals(m.getHomeTeam().getId())) {
                return m.getHomeScore() == null ? 0 : m.getHomeScore();
            }
            return m.getAwayScore() == null ? 0 : m.getAwayScore();
        }).sum();

        int goalsAgainst = teamMatches.stream().mapToInt(m -> {
            if (m.getHomeTeam() != null && teamId.equals(m.getHomeTeam().getId())) {
                return m.getAwayScore() == null ? 0 : m.getAwayScore();
            }
            return m.getHomeScore() == null ? 0 : m.getHomeScore();
        }).sum();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("teamId", teamId);
        response.put("matches", teamMatches.size());
        response.put("played", played);
        response.put("goalsFor", goalsFor);
        response.put("goalsAgainst", goalsAgainst);
        response.put("goalDifference", goalsFor - goalsAgainst);

        return ResponseEntity.ok(response);
    }
}
