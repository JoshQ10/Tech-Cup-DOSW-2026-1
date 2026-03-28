package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Equipos", description = "Endpoints para gestión de equipos y plantilla")
public class TeamController {

        private final TeamService teamService;

        @PostMapping
        @Operation(summary = "Crear equipo", description = "Crea un nuevo equipo de fútbol")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "409", description = "Equipo ya existe")
        })
        public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
                log.info("REST create team endpoint called");
                TeamResponse response = teamService.create(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener equipo", description = "Obtiene los detalles de un equipo por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
                        @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
        })
        public ResponseEntity<TeamResponse> getById(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable String id) {
                log.info("REST get team endpoint called for id: {}", id);
                TeamResponse response = teamService.getById(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/roster")
        @Operation(summary = "Ver plantilla del equipo", description = "Consulta la plantilla actual del equipo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Plantilla obtenida exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
        })
        public ResponseEntity<TeamResponse> getRoster(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable String id) {
                log.info("REST get roster endpoint called for team: {}", id);
                TeamResponse response = teamService.getRoster(id);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}/players/{playerId}")
        @Operation(summary = "Remover jugador del equipo", description = "Elimina un jugador de la plantilla del equipo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Jugador removido exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Equipo o jugador no encontrado")
        })
        public ResponseEntity<TeamResponse> removePlayer(
                        @Parameter(description = "ID del equipo", required = true) @PathVariable("id") String teamId,
                        @Parameter(description = "ID del jugador a remover", required = true) @PathVariable String playerId) {
                log.info("REST remove player endpoint called for team: {} and player: {}", teamId, playerId);
                TeamResponse response = teamService.removePlayer(teamId, playerId);
                return ResponseEntity.ok(response);
        }
}
