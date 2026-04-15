package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.SanctionEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SanctionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
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

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sanctions")
@RequiredArgsConstructor
@Tag(name = "Sanctions", description = "Sanction management")
public class SanctionController {

    private final SanctionRepository sanctionRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List sanctions", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(@RequestParam(required = false) Long tournamentId) {
        List<SanctionEntity> sanctions = tournamentId == null
                ? sanctionRepository.findAll()
                : sanctionRepository.findByTournamentId(tournamentId);
        return ResponseEntity.ok(sanctions.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get sanction", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        SanctionEntity sanction = sanctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sanction not found"));
        return ResponseEntity.ok(toMap(sanction));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','REFEREE')")
    @Operation(summary = "Create sanction", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, REFEREE")
    public ResponseEntity<Map<String, Object>> create(@RequestBody SanctionWriteRequest request) {
        SanctionEntity sanction = new SanctionEntity();
        applyChanges(sanction, request);
        SanctionEntity saved = sanctionRepository.save(sanction);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','REFEREE')")
    @Operation(summary = "Update sanction", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, REFEREE")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestBody SanctionWriteRequest request) {
        SanctionEntity sanction = sanctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sanction not found"));
        applyChanges(sanction, request);
        SanctionEntity updated = sanctionRepository.save(sanction);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete sanction", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!sanctionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sanction not found");
        }
        sanctionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void applyChanges(SanctionEntity sanction, SanctionWriteRequest request) {
        if (request.tournamentId() != null) {
            TournamentEntity tournament = tournamentRepository.findById(request.tournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
            sanction.setTournament(tournament);
        }
        if (request.playerId() != null) {
            UserEntity player = userRepository.findById(request.playerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found"));
            sanction.setPlayer(player);
        }
        if (request.matchId() != null) {
            MatchEntity match = matchRepository.findById(request.matchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
            sanction.setMatch(match);
        }
        if (request.description() != null)
            sanction.setDescription(request.description());
        if (request.sanctionDate() != null)
            sanction.setSanctionDate(request.sanctionDate());
        if (request.suspendedMatches() != null)
            sanction.setSuspendedMatches(request.suspendedMatches());
    }

    private Map<String, Object> toMap(SanctionEntity sanction) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", sanction.getId());
        data.put("tournamentId", sanction.getTournament() != null ? sanction.getTournament().getId() : null);
        data.put("playerId", sanction.getPlayer() != null ? sanction.getPlayer().getId() : null);
        data.put("matchId", sanction.getMatch() != null ? sanction.getMatch().getId() : null);
        data.put("description", sanction.getDescription());
        data.put("sanctionDate", sanction.getSanctionDate());
        data.put("suspendedMatches", sanction.getSuspendedMatches());
        return data;
    }

    public record SanctionWriteRequest(
            Long tournamentId,
            Long playerId,
            Long matchId,
            String description,
            LocalDate sanctionDate,
            Integer suspendedMatches) {
    }
}
