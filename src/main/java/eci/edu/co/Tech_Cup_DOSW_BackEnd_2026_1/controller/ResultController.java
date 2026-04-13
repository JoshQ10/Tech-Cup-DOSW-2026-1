package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@Tag(name = "Results", description = "Match result management")
public class ResultController {

    private final MatchRepository matchRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List results", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list() {
        List<Map<String, Object>> results = matchRepository.findAll().stream()
                .map(this::toResultMap)
                .toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{matchId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get result", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getByMatchId(@Parameter(required = true) @PathVariable Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        return ResponseEntity.ok(toResultMap(match));
    }

    @PostMapping
    @PreAuthorize("hasRole('REFEREE')")
    @Operation(summary = "Create result", description = "Allowed roles: REFEREE")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ResultWriteRequest request) {
        MatchEntity match = matchRepository.findById(request.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        match.setHomeScore(request.homeScore());
        match.setAwayScore(request.awayScore());
        match.setPlayed(true);

        MatchEntity saved = matchRepository.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResultMap(saved));
    }

    @PutMapping("/{matchId}")
    @PreAuthorize("hasAnyRole('REFEREE','ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Update result", description = "Allowed roles: REFEREE, ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long matchId,
            @RequestBody ResultWriteRequest request) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (request.homeScore() != null)
            match.setHomeScore(request.homeScore());
        if (request.awayScore() != null)
            match.setAwayScore(request.awayScore());
        match.setPlayed(true);

        MatchEntity updated = matchRepository.save(match);
        return ResponseEntity.ok(toResultMap(updated));
    }

    @DeleteMapping("/{matchId}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMINISTRATOR')")
    @Operation(summary = "Delete result", description = "Allowed roles: ORGANIZER, ADMINISTRATOR")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        match.setHomeScore(null);
        match.setAwayScore(null);
        match.setPlayed(false);
        matchRepository.save(match);

        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toResultMap(MatchEntity match) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("matchId", match.getId());
        data.put("homeTeamId", match.getHomeTeam() != null ? match.getHomeTeam().getId() : null);
        data.put("awayTeamId", match.getAwayTeam() != null ? match.getAwayTeam().getId() : null);
        data.put("homeScore", match.getHomeScore());
        data.put("awayScore", match.getAwayScore());
        data.put("played", match.isPlayed());
        return data;
    }

    public record ResultWriteRequest(Long matchId, Integer homeScore, Integer awayScore) {
    }
}
