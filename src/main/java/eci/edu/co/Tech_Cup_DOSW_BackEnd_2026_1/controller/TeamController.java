package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TeamService;
import lombok.RequiredArgsConstructor;
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
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = teamService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable String id) {
        TeamResponse response = teamService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamResponse> removePlayer(@PathVariable String teamId,
                                                     @PathVariable String playerId) {
        TeamResponse response = teamService.removePlayer(teamId, playerId);
        return ResponseEntity.ok(response);
    }
}
