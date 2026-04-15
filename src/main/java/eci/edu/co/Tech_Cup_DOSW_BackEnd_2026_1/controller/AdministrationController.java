package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin/config")
@Tag(name = "Administration", description = "General administration configuration")
public class AdministrationController {

    private static final Map<String, String> CONFIG = new ConcurrentHashMap<>();

    static {
        CONFIG.put("registration.open", "true");
        CONFIG.put("payments.autoApprove", "false");
        CONFIG.put("matches.defaultDuration", "90");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "List configuration", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, String>> list() {
        return ResponseEntity.ok(new LinkedHashMap<>(CONFIG));
    }

    @GetMapping("/{key}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Get configuration value", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, String>> getOne(
            @Parameter(required = true) @PathVariable String key) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("key", key);
        data.put("value", CONFIG.get(key));
        return ResponseEntity.ok(data);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create configuration value", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Map<String, String>> create(@RequestBody ConfigWriteRequest request) {
        CONFIG.put(request.key(), request.value());
        Map<String, String> data = new LinkedHashMap<>();
        data.put("key", request.key());
        data.put("value", request.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PutMapping("/{key}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Update configuration value", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, String>> update(
            @Parameter(required = true) @PathVariable String key,
            @RequestBody ConfigUpdateRequest request) {
        String current = CONFIG.get(key);
        if (current == null) {
            return ResponseEntity.notFound().build();
        }
        CONFIG.put(key, request.value());
        Map<String, String> data = new LinkedHashMap<>();
        data.put("key", key);
        data.put("value", request.value());
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete configuration value", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable String key) {
        CONFIG.remove(key);
        return ResponseEntity.noContent().build();
    }

    public record ConfigWriteRequest(String key, String value) {
    }

    public record ConfigUpdateRequest(String value) {
    }
}
