package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
@Tag(name = "Standings", description = "Tournament standings and classification")
public class StandingController {

    private final StandingRepository standingRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List standings", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) Long tournamentId) {
        List<StandingEntity> standings = tournamentId == null
                ? standingRepository.findAll()
                : standingRepository.findByTournamentIdOrderByPointsDescGoalDifferenceDesc(tournamentId);
        return ResponseEntity.ok(standings.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get standing", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        StandingEntity standing = standingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Standing not found"));
        return ResponseEntity.ok(toMap(standing));
    }

    private Map<String, Object> toMap(StandingEntity standing) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", standing.getId());
        data.put("tournamentId", standing.getTournament() != null ? standing.getTournament().getId() : null);
        data.put("teamId", standing.getTeam() != null ? standing.getTeam().getId() : null);
        data.put("played", standing.getPlayed());
        data.put("won", standing.getWon());
        data.put("drawn", standing.getDrawn());
        data.put("lost", standing.getLost());
        data.put("goalsFor", standing.getGoalsFor());
        data.put("goalsAgainst", standing.getGoalsAgainst());
        data.put("goalDifference", standing.getGoalDifference());
        data.put("points", standing.getPoints());
        return data;
    }
}
