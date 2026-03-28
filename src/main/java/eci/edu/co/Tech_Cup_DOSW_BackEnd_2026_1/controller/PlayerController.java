package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Jugadores", description = "Endpoints para gestión de perfil deportivo y disponibilidad")
public class PlayerController {

        private final PlayerService playerService;

        @PutMapping("/{id}/profile")
        @Operation(summary = "Actualizar perfil deportivo", description = "Actualiza la información del perfil deportivo del jugador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
        })
        public ResponseEntity<ProfileResponse> updateProfile(
                        @Parameter(description = "ID del jugador", required = true) @PathVariable String id,
                        @Valid @RequestBody ProfileRequest request) {
                log.info("REST update profile endpoint called for player: {}", id);
                ProfileResponse response = playerService.updateProfile(id, request);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/{id}/photo")
        @Operation(summary = "Subir foto de perfil", description = "Actualiza la foto de perfil del jugador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
        })
        public ResponseEntity<ProfileResponse> uploadPhoto(
                        @Parameter(description = "ID del jugador", required = true) @PathVariable String id,
                        @Valid @RequestBody PhotoUploadRequest request) {
                log.info("REST upload photo endpoint called for player: {}", id);
                ProfileResponse response = playerService.uploadPhoto(id, request);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/availability")
        @Operation(summary = "Cambiar disponibilidad", description = "Actualiza el estado de disponibilidad del jugador para torneos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
        })
        public ResponseEntity<ProfileResponse> changeAvailability(
                        @Parameter(description = "ID del jugador", required = true) @PathVariable String id,
                        @Valid @RequestBody AvailabilityRequest request) {
                log.info("REST change availability endpoint called for player: {}", id);
                ProfileResponse response = playerService.changeAvailability(id, request);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/profile")
        @Operation(summary = "Consultar perfil de un jugador", description = "Retorna el perfil deportivo de un jugador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
                        @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
        })
        public ResponseEntity<ProfileResponse> getProfile(
                        @Parameter(description = "ID del jugador", required = true) @PathVariable String id) {
                log.info("REST get profile endpoint called for player: {}", id);
                ProfileResponse response = playerService.getProfile(id);
                return ResponseEntity.ok(response);
        }
}
