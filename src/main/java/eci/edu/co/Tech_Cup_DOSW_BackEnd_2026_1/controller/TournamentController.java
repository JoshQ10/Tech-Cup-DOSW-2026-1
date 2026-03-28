package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Torneos", description = "Endpoints para creación y administración de torneos")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @Operation(summary = "Crear torneo", description = "Crea un nuevo torneo de fútbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Torneo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Torneo ya existe")
    })
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentRequest request) {
        log.info("REST create tournament endpoint called");
        TournamentResponse response = tournamentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener torneo", description = "Obtiene los detalles de un torneo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<TournamentResponse> getById(
            @Parameter(description = "ID del torneo", required = true) @PathVariable String id) {
        log.info("REST get tournament endpoint called for id: {}", id);
        TournamentResponse response = tournamentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/config")
    @Operation(summary = "Configurar torneo", description = "Actualiza la configuracion base del torneo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo configurado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<TournamentResponse> configure(
            @Parameter(description = "ID del torneo", required = true) @PathVariable String id,
            @Valid @RequestBody TournamentConfigRequest request) {
        log.info("REST configure tournament endpoint called for id: {}", id);
        TournamentResponse response = tournamentService.configure(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Cambiar estado del torneo", description = "Modifica el estado actual del torneo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Transición de estado inválida"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<TournamentResponse> changeStatus(
            @Parameter(description = "ID del torneo", required = true) @PathVariable String id,
            @Valid @RequestBody ChangeStatusRequest request) {
        log.info("REST change tournament status endpoint called for id: {}", id);
        TournamentResponse response = tournamentService.changeStatus(id, request);
        return ResponseEntity.ok(response);
    }
}
