package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match-events")
@RequiredArgsConstructor
@Tag(name = "Match Events", description = "Events registered during matches")
public class MatchEventController {

    private final MatchEventRepository matchEventRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List match events", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) Long matchId,
            @RequestParam(required = false) String eventType) {
        List<MatchEventEntity> events;
        if (matchId != null && eventType != null && !eventType.isBlank()) {
            events = matchEventRepository.findByMatchIdAndEventType(matchId, MatchEventType.valueOf(eventType));
        } else if (matchId != null) {
            events = matchEventRepository.findByMatchId(matchId);
        } else {
            events = matchEventRepository.findAll();
        }
        return ResponseEntity.ok(events.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get match event", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        MatchEventEntity event = matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match event not found"));
        return ResponseEntity.ok(toMap(event));
    }

    @PostMapping
    @PreAuthorize("hasRole('REFEREE')")
    @Operation(summary = "Create match event", description = "Allowed roles: REFEREE")
    public ResponseEntity<Map<String, Object>> create(@RequestBody MatchEventWriteRequest request) {
        MatchEventEntity entity = new MatchEventEntity();
        applyChanges(entity, request);
        MatchEventEntity saved = matchEventRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','REFEREE')")
    @Operation(summary = "Update match event", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, REFEREE")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody MatchEventWriteRequest request) {
        MatchEventEntity event = matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match event not found"));
        applyChanges(event, request);
        MatchEventEntity updated = matchEventRepository.save(event);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Delete match event", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!matchEventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match event not found");
        }
        matchEventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void applyChanges(MatchEventEntity entity, MatchEventWriteRequest request) {
        if (request.matchId() != null) {
            MatchEntity match = matchRepository.findById(request.matchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
            entity.setMatch(match);
        }
        if (request.playerId() != null) {
            UserEntity player = userRepository.findById(request.playerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found"));
            entity.setPlayer(player);
        }
        if (request.teamId() != null) {
            TeamEntity team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
            entity.setTeam(team);
        }
        if (request.eventType() != null) {
            entity.setEventType(request.eventType());
        }
        if (request.minute() != null) {
            entity.setMinute(request.minute());
        }
    }

    private Map<String, Object> toMap(MatchEventEntity event) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", event.getId());
        data.put("matchId", event.getMatch() != null ? event.getMatch().getId() : null);
        data.put("playerId", event.getPlayer() != null ? event.getPlayer().getId() : null);
        data.put("teamId", event.getTeam() != null ? event.getTeam().getId() : null);
        data.put("eventType", event.getEventType());
        data.put("minute", event.getMinute());
        return data;
    }

    public record MatchEventWriteRequest(
            Long matchId,
            Long playerId,
            Long teamId,
            MatchEventType eventType,
            Integer minute) {
    }
}
