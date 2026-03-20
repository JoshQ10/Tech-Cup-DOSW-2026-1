package edu.dosw.proyecto.Tech_Cup_Football_2026_1.controller;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.LoginRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.LoginResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.service.RegistroService;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.service.VerificationService;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final RegistroService registroService;
    private final VerificationService verificationService;

    public AuthController(RegistroService registroService, VerificationService verificationService, AuthService authService) {
        this.registroService = registroService;
        this.verificationService = verificationService;
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistroRequest solicitud) {
        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);
        if (respuesta.isExitoso()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {
        String resultado = verificationService.verificarToken(token);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<TokenVerificacion> reenviarToken(@RequestParam Long usuarioId) {
        TokenVerificacion token = verificationService.reenviarToken(usuarioId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}