package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentRequest request) {
        TournamentResponse response = tournamentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getById(@PathVariable String id) {
        TournamentResponse response = tournamentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TournamentResponse> changeStatus(@PathVariable String id,
                                                           @Valid @RequestBody ChangeStatusRequest request) {
        TournamentResponse response = tournamentService.changeStatus(id, request);
        return ResponseEntity.ok(response);
    }
}
