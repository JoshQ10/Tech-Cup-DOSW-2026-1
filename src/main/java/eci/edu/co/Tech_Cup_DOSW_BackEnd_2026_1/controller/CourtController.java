package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
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
@RequestMapping("/api/courts")
@RequiredArgsConstructor
@Tag(name = "Courts", description = "Court management")
public class CourtController {

    private final CourtRepository courtRepository;
    private final TournamentRepository tournamentRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List courts", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) Long tournamentId) {
        List<CourtEntity> courts = tournamentId == null
                ? courtRepository.findAll()
                : courtRepository.findByTournamentId(tournamentId);
        return ResponseEntity.ok(courts.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get court", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(
            @Parameter(required = true) @PathVariable Long id) {
        CourtEntity court = courtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));
        return ResponseEntity.ok(toMap(court));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Create court", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> create(@RequestBody CourtWriteRequest request) {
        CourtEntity court = new CourtEntity();
        applyChanges(court, request);
        CourtEntity saved = courtRepository.save(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Update court", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody CourtWriteRequest request) {
        CourtEntity court = courtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));
        applyChanges(court, request);
        CourtEntity updated = courtRepository.save(court);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Delete court", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!courtRepository.existsById(id)) {
            throw new ResourceNotFoundException("Court not found");
        }
        courtRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void applyChanges(CourtEntity court, CourtWriteRequest request) {
        if (request.name() != null)
            court.setName(request.name());
        if (request.location() != null)
            court.setLocation(request.location());
        if (request.tournamentId() != null) {
            TournamentEntity tournament = tournamentRepository.findById(request.tournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
            court.setTournament(tournament);
        }
    }

    private Map<String, Object> toMap(CourtEntity court) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", court.getId());
        data.put("name", court.getName());
        data.put("location", court.getLocation());
        data.put("tournamentId", court.getTournament() != null ? court.getTournament().getId() : null);
        return data;
    }

    public record CourtWriteRequest(Long tournamentId, String name, String location) {
    }
}
