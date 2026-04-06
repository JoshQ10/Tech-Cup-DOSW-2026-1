package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Teams", description = "Endpoints for team management and roster")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Create team", description = "Creates a new football team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "No permission for this operation"),
            @ApiResponse(responseCode = "409", description = "Team already exists")
    })
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        log.info("REST create team endpoint called");
        TeamResponse response = teamService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team", description = "Retrieves the details of a team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<TeamResponse> getById(
            @Parameter(description = "ID del equipo", required = true) @PathVariable Long id) {
        log.info("REST get team endpoint called for id: {}", id);
        TeamResponse response = teamService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/roster")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team roster", description = "Retrieves the current roster of the team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roster successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<TeamResponse> getRoster(
            @Parameter(description = "ID del equipo", required = true) @PathVariable Long id) {
        log.info("REST get roster endpoint called for team: {}", id);
        TeamResponse response = teamService.getRoster(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/players/{playerId}")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Remove player from team", description = "Removes a player from the team roster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player successfully removed"),
            @ApiResponse(responseCode = "403", description = "No permission for this operation"),
            @ApiResponse(responseCode = "404", description = "Team or player not found")
    })
    public ResponseEntity<TeamResponse> removePlayer(
            @Parameter(description = "ID del equipo", required = true) @PathVariable("id") Long teamId,
            @Parameter(description = "ID del jugador a remover", required = true) @PathVariable Long playerId) {
        log.info("REST remove player endpoint called for team: {} and player: {}", teamId, playerId);
        TeamResponse response = teamService.removePlayer(teamId, playerId);
        return ResponseEntity.ok(response);
    }
}
