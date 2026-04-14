package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.EliminationBracketEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.EliminationBracketRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elimination-brackets")
@RequiredArgsConstructor
@Tag(name = "Elimination Brackets", description = "Tournament elimination bracket management")
public class EliminationBracketController {

    private final EliminationBracketRepository eliminationBracketRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List elimination brackets", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) Long tournamentId,
            @RequestParam(required = false) String round) {
        List<EliminationBracketEntity> brackets;
        if (tournamentId != null && round != null && !round.isBlank()) {
            brackets = eliminationBracketRepository.findByTournamentIdAndRound(tournamentId, round);
        } else if (tournamentId != null) {
            brackets = eliminationBracketRepository.findByTournamentId(tournamentId);
        } else {
            brackets = eliminationBracketRepository.findAll();
        }
        return ResponseEntity.ok(brackets.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get elimination bracket", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(
            @Parameter(required = true) @PathVariable Long id) {
        EliminationBracketEntity bracket = eliminationBracketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Elimination bracket not found"));
        return ResponseEntity.ok(toMap(bracket));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Create elimination bracket", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> create(@RequestBody BracketWriteRequest request) {
        EliminationBracketEntity bracket = new EliminationBracketEntity();
        applyChanges(bracket, request);
        EliminationBracketEntity saved = eliminationBracketRepository.save(bracket);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Update elimination bracket", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody BracketWriteRequest request) {
        EliminationBracketEntity bracket = eliminationBracketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Elimination bracket not found"));
        applyChanges(bracket, request);
        EliminationBracketEntity updated = eliminationBracketRepository.save(bracket);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Delete elimination bracket", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!eliminationBracketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Elimination bracket not found");
        }
        eliminationBracketRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void applyChanges(EliminationBracketEntity bracket, BracketWriteRequest request) {
        if (request.tournamentId() != null) {
            TournamentEntity tournament = tournamentRepository.findById(request.tournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
            bracket.setTournament(tournament);
        }
        if (request.matchId() != null) {
            MatchEntity match = matchRepository.findById(request.matchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
            bracket.setMatch(match);
        }
        if (request.round() != null)
            bracket.setRound(request.round());
        if (request.matchPosition() != null)
            bracket.setMatchPosition(request.matchPosition());
    }

    private Map<String, Object> toMap(EliminationBracketEntity bracket) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", bracket.getId());
        data.put("tournamentId", bracket.getTournament() != null ? bracket.getTournament().getId() : null);
        data.put("round", bracket.getRound());
        data.put("matchPosition", bracket.getMatchPosition());
        data.put("matchId", bracket.getMatch() != null ? bracket.getMatch().getId() : null);
        return data;
    }

    public record BracketWriteRequest(Long tournamentId, Long matchId, String round, Integer matchPosition) {
    }
}
