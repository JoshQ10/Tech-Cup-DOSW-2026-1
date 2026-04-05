package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Jugadores", description = "Endpoints para gestion de perfil deportivo y disponibilidad")
public class PlayerController {

    private final PlayerService playerService;

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN')")
    @Operation(summary = "Actualizar perfil deportivo", description = "Actualiza la informacion del perfil deportivo del jugador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operacion"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public ResponseEntity<ProfileResponse> updateProfile(
            @Parameter(description = "ID del jugador", required = true) @PathVariable Long id,
            @Valid @RequestBody ProfileRequest request) {
        log.info("REST update profile endpoint called for player: {}", id);
        ProfileResponse response = playerService.updateProfile(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/photo")
    @PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN')")
    @Operation(summary = "Subir foto de perfil", description = "Actualiza la foto de perfil del jugador usando base64. La foto debe estar en formato data:image/[jpeg|png];base64,...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Foto invalida: formato no soportado, tamano excedido (max 5MB) o base64 invalido"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operacion"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public ResponseEntity<ProfileResponse> uploadPhoto(
            @Parameter(description = "ID del jugador", required = true) @PathVariable Long id,
            @Valid @RequestBody PhotoUploadRequest request) {
        log.info("REST upload photo endpoint called for player: {}", id);
        ProfileResponse response = playerService.uploadPhoto(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN')")
    @Operation(summary = "Cambiar disponibilidad",
               description = "Actualiza el estado de disponibilidad del jugador para torneos. Registra el timestamp y razon del cambio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operacion"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public ResponseEntity<ProfileResponse> changeAvailability(
            @Parameter(description = "ID del jugador", required = true) @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequest request) {
        log.info("REST change availability endpoint called for player: {}", id);
        ProfileResponse response = playerService.changeAvailability(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consultar perfil de un jugador", description = "Retorna el perfil deportivo de un jugador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public ResponseEntity<ProfileResponse> getProfile(
            @Parameter(description = "ID del jugador", required = true) @PathVariable Long id) {
        log.info("REST get profile endpoint called for player: {}", id);
        ProfileResponse response = playerService.getProfile(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'PLAYER', 'ADMINISTRATOR')")
    @Operation(summary = "Buscar jugadores disponibles",
               description = "Busca jugadores disponibles filtrados por posición, semestre, edad, género o nombre con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para esta operacion")
    })
    public ResponseEntity<PlayerSearchResponse> searchPlayers(
            @Parameter(description = "Posición del jugador") @RequestParam(required = false) Position position,
            @Parameter(description = "Semestre académico") @RequestParam(required = false) Integer semester,
            @Parameter(description = "Edad del jugador") @RequestParam(required = false) Integer age,
            @Parameter(description = "Género del jugador") @RequestParam(required = false) String gender,
            @Parameter(description = "Nombre del jugador (búsqueda parcial)") @RequestParam(required = false) String name,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.info("REST search available players endpoint called - position: {}, semester: {}, age: {}, gender: {}, name: {}",
                position, semester, age, gender, name);
        PlayerSearchResponse response = playerService.searchAvailablePlayers(position, semester, age, gender, name, pageable);
        return ResponseEntity.ok(response);
    }
}
