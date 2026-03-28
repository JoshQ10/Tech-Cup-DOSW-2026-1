package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
