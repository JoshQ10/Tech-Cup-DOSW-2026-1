package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tournaments", description = "Endpoints for tournament creation and administration")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Create tournament", description = "Creates a new football tournament")
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

    @PutMapping("/{id}/config")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Configure tournament", description = "Updates the base configuration of the tournament")
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
    @Operation(summary = "Setup tournament", description = "Configures rules, courts, schedules and sanctions for the tournament")
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
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Change tournament status", description = "Modifies the current status of the tournament")
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
}
