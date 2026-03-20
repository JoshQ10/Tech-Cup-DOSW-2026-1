package edu.dosw.proyecto.Tech_Cup_Football_2026_1.controller;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.service.RegistroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final RegistroService registroService;

    public AuthController(RegistroService registroService) {
        this.registroService = registroService;
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

    @GetMapping("/verificar")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {
        // Esta funcionalidad se completará en la siguiente fase
        return ResponseEntity.ok("Email verificado exitosamente");
    }
}
