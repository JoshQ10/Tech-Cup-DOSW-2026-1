package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/ssl")
    public ResponseEntity<String> testSSL() {
        return ResponseEntity.ok("✅ HTTPS/SSL funcionando correctamente en puerto 8443");
    }

    @GetMapping("/cors")
    public ResponseEntity<String> testCORS() {
        return ResponseEntity.ok("✅ CORS habilitado - puedes acceder desde localhost con credenciales");
    }
}
