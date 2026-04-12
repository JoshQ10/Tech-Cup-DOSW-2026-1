package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Match management")
public class MatchController {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List matches", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(@RequestParam(required = false) Long tournamentId) {
        List<MatchEntity> matches = tournamentId == null
                ? matchRepository.findAll()
                : matchRepository.findByTournamentId(tournamentId);
        return ResponseEntity.ok(matches.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get match", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        MatchEntity match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        return ResponseEntity.ok(toMap(match));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Create match", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> create(@RequestBody MatchWriteRequest request) {
        MatchEntity saved = matchRepository.save(buildNewEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','REFEREE')")
    @Operation(summary = "Update match", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, REFEREE")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody MatchWriteRequest request) {
        MatchEntity current = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        applyChanges(current, request);
        MatchEntity updated = matchRepository.save(current);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Delete match", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!matchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match not found");
        }
        matchRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private MatchEntity buildNewEntity(MatchWriteRequest request) {
        MatchEntity entity = new MatchEntity();
        applyChanges(entity, request);
        return entity;
    }

    private void applyChanges(MatchEntity entity, MatchWriteRequest request) {
        if (request.tournamentId() != null) {
            TournamentEntity tournament = tournamentRepository.findById(request.tournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
            entity.setTournament(tournament);
        }
        if (request.homeTeamId() != null) {
            TeamEntity home = teamRepository.findById(request.homeTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Home team not found"));
            entity.setHomeTeam(home);
        }
        if (request.awayTeamId() != null) {
            TeamEntity away = teamRepository.findById(request.awayTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Away team not found"));
            entity.setAwayTeam(away);
        }
        if (request.refereeId() != null) {
            UserEntity referee = userRepository.findById(request.refereeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Referee not found"));
            entity.setReferee(referee);
        }
        if (request.courtId() != null) {
            CourtEntity court = courtRepository.findById(request.courtId())
                    .orElseThrow(() -> new ResourceNotFoundException("Court not found"));
            entity.setCourt(court);
        }

        if (request.matchDate() != null)
            entity.setMatchDate(request.matchDate());
        if (request.matchTime() != null)
            entity.setMatchTime(request.matchTime());
        if (request.phase() != null)
            entity.setPhase(request.phase());
        if (request.homeScore() != null)
            entity.setHomeScore(request.homeScore());
        if (request.awayScore() != null)
            entity.setAwayScore(request.awayScore());
        if (request.played() != null)
            entity.setPlayed(request.played());
    }

    private Map<String, Object> toMap(MatchEntity match) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", match.getId());
        data.put("tournamentId", match.getTournament() != null ? match.getTournament().getId() : null);
        data.put("homeTeamId", match.getHomeTeam() != null ? match.getHomeTeam().getId() : null);
        data.put("awayTeamId", match.getAwayTeam() != null ? match.getAwayTeam().getId() : null);
        data.put("refereeId", match.getReferee() != null ? match.getReferee().getId() : null);
        data.put("courtId", match.getCourt() != null ? match.getCourt().getId() : null);
        data.put("matchDate", match.getMatchDate());
        data.put("matchTime", match.getMatchTime());
        data.put("phase", match.getPhase());
        data.put("homeScore", match.getHomeScore());
        data.put("awayScore", match.getAwayScore());
        data.put("played", match.isPlayed());
        return data;
    }

    public record MatchWriteRequest(
            Long tournamentId,
            Long homeTeamId,
            Long awayTeamId,
            Long refereeId,
            Long courtId,
            java.time.LocalDate matchDate,
            java.time.LocalTime matchTime,
            MatchPhase phase,
            Integer homeScore,
            Integer awayScore,
            Boolean played) {
    }
}
