package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.LineupPlayerEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.LineupPlayerRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.LineupRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
@Tag(name = "Lineups", description = "Lineup and lineup players")
public class LineupController {

    private final LineupRepository lineupRepository;
    private final LineupPlayerRepository lineupPlayerRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List lineups", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list() {
        return ResponseEntity.ok(lineupRepository.findAll().stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get lineup", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        LineupEntity lineup = lineupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lineup not found"));
        return ResponseEntity.ok(toMap(lineup));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CAPTAIN','ADMINISTRATOR')")
    @Operation(summary = "Create lineup", description = "Allowed roles: CAPTAIN, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> create(@RequestBody LineupWriteRequest request,
            Authentication authentication) {
        MatchEntity match = matchRepository.findById(request.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        TeamEntity team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        assertCaptainOwnsTeam(authentication, team);

        LineupEntity lineup = LineupEntity.builder()
                .match(match)
                .team(team)
                .formation(request.formation())
                .build();

        LineupEntity saved = lineupRepository.save(lineup);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CAPTAIN','ADMINISTRATOR')")
    @Operation(summary = "Update lineup", description = "Allowed roles: CAPTAIN, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody LineupWriteRequest request,
            Authentication authentication) {
        LineupEntity lineup = lineupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lineup not found"));
        assertCaptainOwnsTeam(authentication, lineup.getTeam());
        if (request.formation() != null)
            lineup.setFormation(request.formation());
        LineupEntity updated = lineupRepository.save(lineup);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete lineup", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!lineupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lineup not found");
        }
        lineupPlayerRepository.findByLineupId(id).forEach(lp -> lineupPlayerRepository.deleteById(lp.getId()));
        lineupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/players")
    @PreAuthorize("hasAnyRole('CAPTAIN','ADMINISTRATOR')")
    @Operation(summary = "Add player to lineup", description = "Allowed roles: CAPTAIN, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> addPlayer(
            @PathVariable Long id,
            @RequestBody LineupPlayerWriteRequest request,
            Authentication authentication) {
        LineupEntity lineup = lineupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lineup not found"));
        assertCaptainOwnsTeam(authentication, lineup.getTeam());
        UserEntity player = userRepository.findById(request.playerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        LineupPlayerEntity entity = LineupPlayerEntity.builder()
                .lineup(lineup)
                .player(player)
                .positionX(request.positionX())
                .positionY(request.positionY())
                .starter(request.starter() != null && request.starter())
                .build();

        LineupPlayerEntity saved = lineupPlayerRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toLineupPlayerMap(saved));
    }

    @PutMapping("/{id}/players/{lineupPlayerId}")
    @PreAuthorize("hasAnyRole('CAPTAIN','ADMINISTRATOR')")
    @Operation(summary = "Update lineup player", description = "Allowed roles: CAPTAIN, ADMINISTRATOR")
    public ResponseEntity<Map<String, Object>> updateLineupPlayer(
            @PathVariable Long id,
            @PathVariable Long lineupPlayerId,
            @RequestBody LineupPlayerWriteRequest request,
            Authentication authentication) {
        if (!lineupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lineup not found");
        }
        LineupEntity lineup = lineupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lineup not found"));
        assertCaptainOwnsTeam(authentication, lineup.getTeam());
        LineupPlayerEntity entity = lineupPlayerRepository.findById(lineupPlayerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lineup player not found"));

        if (request.positionX() != null)
            entity.setPositionX(request.positionX());
        if (request.positionY() != null)
            entity.setPositionY(request.positionY());
        if (request.starter() != null)
            entity.setStarter(request.starter());
        LineupPlayerEntity updated = lineupPlayerRepository.save(entity);
        return ResponseEntity.ok(toLineupPlayerMap(updated));
    }

    private Map<String, Object> toMap(LineupEntity lineup) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", lineup.getId());
        data.put("matchId", lineup.getMatch() != null ? lineup.getMatch().getId() : null);
        data.put("teamId", lineup.getTeam() != null ? lineup.getTeam().getId() : null);
        data.put("formation", lineup.getFormation());
        data.put("players",
                lineupPlayerRepository.findByLineupId(lineup.getId()).stream().map(this::toLineupPlayerMap).toList());
        return data;
    }

    private Map<String, Object> toLineupPlayerMap(LineupPlayerEntity entity) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", entity.getId());
        data.put("lineupId", entity.getLineup() != null ? entity.getLineup().getId() : null);
        data.put("playerId", entity.getPlayer() != null ? entity.getPlayer().getId() : null);
        data.put("positionX", entity.getPositionX());
        data.put("positionY", entity.getPositionY());
        data.put("starter", entity.isStarter());
        return data;
    }

    public record LineupWriteRequest(Long matchId, Long teamId, String formation) {
    }

    public record LineupPlayerWriteRequest(Long playerId, Integer positionX, Integer positionY, Boolean starter) {
    }

    private void assertCaptainOwnsTeam(Authentication authentication, TeamEntity team) {
        if (!hasRole(authentication, "ROLE_CAPTAIN")) {
            return;
        }
        UserEntity currentUser = getCurrentUser(authentication);
        if (team == null || team.getCaptainId() == null || !team.getCaptainId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Como CAPTAIN solo puedes gestionar alineaciones de tu equipo");
        }
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        return authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> roleName.equals(a.getAuthority()));
    }
}
