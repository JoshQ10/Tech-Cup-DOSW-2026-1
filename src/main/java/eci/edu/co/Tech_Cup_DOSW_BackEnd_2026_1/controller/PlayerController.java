package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PutMapping("/{id}/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable String id, 
                                                         @Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = playerService.updateProfile(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ProfileResponse> changeAvailability(@PathVariable String id,
                                                              @Valid @RequestBody AvailabilityRequest request) {
        ProfileResponse response = playerService.changeAvailability(id, request);
        return ResponseEntity.ok(response);
    }
}
