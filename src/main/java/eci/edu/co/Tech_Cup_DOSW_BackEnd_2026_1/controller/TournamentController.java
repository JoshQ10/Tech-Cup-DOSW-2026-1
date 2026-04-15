package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ActiveTournamentInfoResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentBracketResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentRulesConfirmationResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentStandingRowResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTopScorerResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMatchHistoryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMonthlyPerformanceResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTeamSummaryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tournaments", description = "Endpoints for tournament creation and administration")
public class TournamentController {

        private final TournamentService tournamentService;
        private final TeamService teamService;

        @PostMapping
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Create tournament", description = "Creates a new football tournament. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Tournament successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid data"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "409", description = "Tournament already exists")
        })
        public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentRequest request) {
                log.info("REST create tournament endpoint called");
                TournamentResponse response = tournamentService.create(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get tournament", description = "Retrieves the details of a tournament by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tournament found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentResponse> getById(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament endpoint called for id: {}", id);
                TournamentResponse response = tournamentService.getById(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/active")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get active tournament info", description = "Returns general data of the active tournament including dates, state, enrolled teams, available courts and current phase")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Active tournament info returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "No active tournament found")
        })
        public ResponseEntity<ActiveTournamentInfoResponse> getActiveTournamentInfo() {
                log.info("REST get active tournament info endpoint called");
                ActiveTournamentInfoResponse response = tournamentService.getActiveTournamentInfo();
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/standings")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get standings table", description = "Returns tournament standings ordered by points, goal difference and goals for")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Standings returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<TournamentStandingRowResponse>> getTournamentStandings(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament standings endpoint called for id: {}", id);
                List<TournamentStandingRowResponse> response = tournamentService.getTournamentStandings(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/bracket")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get tournament bracket", description = "Returns bracket structure by phase with classified teams, matchups and results")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Bracket returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentBracketResponse> getTournamentBracket(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament bracket endpoint called for id: {}", id);
                TournamentBracketResponse response = tournamentService.getTournamentBracket(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/cards")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get tournament card history", description = "Returns all yellow/red cards in chronological order")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cards history returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<TournamentCardEventResponse>> getTournamentCards(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament cards endpoint called for id: {}", id);
                List<TournamentCardEventResponse> response = tournamentService.getTournamentCards(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/top-scorers")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get top scorers ranking", description = "Returns tournament top scorers ordered by total goals, including team and player photo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Top scorers ranking returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<TournamentTopScorerResponse>> getTournamentTopScorers(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament top scorers endpoint called for id: {}", id);
                List<TournamentTopScorerResponse> response = tournamentService.getTournamentTopScorers(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/matches")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get tournament matches history", description = "Returns all played tournament matches with teams and final score")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Matches history returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<TournamentMatchHistoryResponse>> getTournamentMatchesHistory(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament matches history endpoint called for id: {}", id);
                List<TournamentMatchHistoryResponse> response = tournamentService.getTournamentMatchHistory(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/performance")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get monthly team performance", description = "Returns team monthly performance data (January-April) for line charts")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Monthly performance returned successfully"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<List<TournamentMonthlyPerformanceResponse>> getTournamentMonthlyPerformance(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament monthly performance endpoint called for id: {}", id);
                List<TournamentMonthlyPerformanceResponse> response = tournamentService
                                .getTournamentMonthlyPerformance(id);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/{id}/confirm-rules")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Confirm tournament rules reading", description = "Registers that authenticated user has read and confirmed tournament rules")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Rules confirmation registered"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Tournament or user not found")
        })
        public ResponseEntity<TournamentRulesConfirmationResponse> confirmRules(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id,
                        Authentication authentication) {
                log.info("REST confirm-rules endpoint called for tournament id: {}", id);
                String authenticatedUser = authentication != null ? authentication.getName() : "anonymous";
                TournamentRulesConfirmationResponse response = tournamentService
                                .confirmTournamentRules(id, authenticatedUser);
                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}/config")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Configure tournament", description = "Updates the base configuration of the tournament. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tournament successfully configured"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentResponse> configure(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id,
                        @Valid @RequestBody TournamentConfigRequest request) {
                log.info("REST configure tournament endpoint called for id: {}", id);
                TournamentResponse response = tournamentService.configure(id, request);
                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}/setup")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Setup tournament", description = "Configures rules, courts, schedules and sanctions for the tournament. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tournament successfully configured"),
                        @ApiResponse(responseCode = "400", description = "Invalid configuration data"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentSetupResponse> setup(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id,
                        @Valid @RequestBody TournamentSetupRequest request) {
                log.info("REST setup tournament endpoint called for id: {}", id);
                TournamentSetupResponse response = tournamentService.setup(id, request);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/status")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Change tournament status", description = "Modifies the current status of the tournament. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Status successfully updated"),
                        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<TournamentResponse> changeStatus(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id,
                        @Valid @RequestBody ChangeStatusRequest request) {
                log.info("REST change tournament status endpoint called for id: {}", id);
                TournamentResponse response = tournamentService.changeStatus(id, request);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
        @Operation(summary = "Delete tournament", description = "Deletes a tournament. Allowed roles: ORGANIZER, ADMINISTRATOR")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Tournament successfully deleted"),
                        @ApiResponse(responseCode = "403", description = "No permission for this operation"),
                        @ApiResponse(responseCode = "404", description = "Tournament not found")
        })
        public ResponseEntity<Void> delete(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST delete tournament endpoint called for id: {}", id);
                tournamentService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}/teams")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Listar equipos inscritos en el torneo", description = "Retorna todos los equipos con escudo, nombre, capitán y número de jugadores. Utilizado en el carrusel de la ficha del torneo y en el bracket.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de equipos retornada exitosamente"),
                        @ApiResponse(responseCode = "401", description = "No autenticado"),
                        @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
        })
        public ResponseEntity<List<TournamentTeamSummaryResponse>> getTeams(
                        @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
                log.info("REST get tournament teams endpoint called for tournament: {}", id);
                List<TournamentTeamSummaryResponse> response = teamService.getTeamsByTournament(id);
                return ResponseEntity.ok(response);
        }
}
