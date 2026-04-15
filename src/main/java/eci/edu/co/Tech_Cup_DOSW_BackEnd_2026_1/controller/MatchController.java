package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchCardRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchGoalRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchPossessionRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchResultRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchDetailResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.MatchPossessionResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.MatchService;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@SuppressWarnings("null")
public class MatchController {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;
    private final MatchService matchService;

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
    @Operation(summary = "Obtener detalle de un partido", description = "Retorna resultado, fecha, hora, cancha, goles (jugador + minuto), tarjetas (tipo + jugador + minuto) y datos de dominación del balón")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle del partido retornado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    public ResponseEntity<MatchDetailResponse> getById(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id) {
        log.info("REST get match detail endpoint called for id: {}", id);
        return ResponseEntity.ok(matchService.getMatchDetail(id));
    }

    @PostMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Registrar resultado del partido", description = "Registra el marcador final y actualiza automáticamente la tabla de posiciones. Allowed roles: ADMINISTRATOR, REFEREE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    public ResponseEntity<MatchDetailResponse> registerResult(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id,
            @Valid @RequestBody MatchResultRequest request) {
        log.info("REST register result endpoint called for match: {}", id);
        return ResponseEntity.ok(matchService.registerResult(id, request));
    }

    @PostMapping("/{id}/goals")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Registrar goles del partido", description = "Registra cada gol con jugador, equipo y minuto exacto (incluyendo tiempo de reposición). Allowed roles: ADMINISTRATOR, REFEREE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goles registrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
            @ApiResponse(responseCode = "404", description = "Partido o jugador no encontrado")
    })
    public ResponseEntity<MatchDetailResponse> registerGoals(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id,
            @Valid @RequestBody MatchGoalRequest request) {
        log.info("REST register goals endpoint called for match: {}", id);
        return ResponseEntity.ok(matchService.registerGoals(id, request));
    }

    @PostMapping("/{id}/cards")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Registrar tarjetas del partido", description = "Registra tarjeta amarilla o roja con jugador, equipo y minuto. Acumula sanciones por jugador en el torneo. Allowed roles: ADMINISTRATOR, REFEREE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarjetas registradas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
            @ApiResponse(responseCode = "404", description = "Partido o jugador no encontrado")
    })
    public ResponseEntity<MatchDetailResponse> registerCards(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id,
            @Valid @RequestBody MatchCardRequest request) {
        log.info("REST register cards endpoint called for match: {}", id);
        return ResponseEntity.ok(matchService.registerCards(id, request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE')")
    @Operation(summary = "Actualizar estado del partido", description = "Cambia el estado: TO_ASSIGN → TO_PLAY → FINISHED / CANCELLED. Al cancelar muestra 'X-X'. Allowed roles: ADMINISTRATOR, ORGANIZER, REFEREE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    public ResponseEntity<MatchDetailResponse> updateStatus(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id,
            @Valid @RequestBody MatchStatusRequest request) {
        log.info("REST update match status endpoint called for match: {}", id);
        return ResponseEntity.ok(matchService.updateStatus(id, request));
    }

    @PostMapping("/{id}/possession")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Registrar dominación del balón", description = "Almacena porcentaje de posesión por equipo y coordenadas del heatmap para visualización. Allowed roles: ADMINISTRATOR, REFEREE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de posesión registrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operación"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    public ResponseEntity<MatchPossessionResponse> registerPossession(
            @Parameter(description = "ID del partido", required = true) @PathVariable Long id,
            @Valid @RequestBody MatchPossessionRequest request) {
        log.info("REST register possession endpoint called for match: {}", id);
        return ResponseEntity.ok(matchService.registerPossession(id, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Create match", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> create(@RequestBody MatchWriteRequest request) {
        MatchEntity saved = matchRepository.save(buildNewEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Update match", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
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
