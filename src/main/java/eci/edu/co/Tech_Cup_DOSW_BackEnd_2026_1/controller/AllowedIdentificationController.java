package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.AllowedIdentificationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.AllowedIdentificationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/allowed-identifications")
@RequiredArgsConstructor
@Tag(name = "Allowed Identifications", description = "Admin management of whitelisted IDs for privileged role registration")
public class AllowedIdentificationController {

    private final AllowedIdentificationRepository allowedIdentificationRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "List allowed identifications", description = "Returns all whitelisted cédulas. Allowed roles: ADMINISTRATOR")
    public ResponseEntity<List<AllowedIdentificationEntity>> list(
            @RequestParam(required = false) Role role) {
        List<AllowedIdentificationEntity> entries = role == null
                ? allowedIdentificationRepository.findAll()
                : allowedIdentificationRepository.findByAllowedRole(role);
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Add allowed identification",
            description = "Registers a cédula as authorized for a privileged role (ADMINISTRATOR, REFEREE, ORGANIZER). Allowed roles: ADMINISTRATOR")
    public ResponseEntity<AllowedIdentificationEntity> create(@RequestBody AllowedIdRequest request) {
        if (request.identification() == null || request.identification().isBlank()) {
            throw new BusinessRuleException("La cédula es requerida");
        }
        if (!request.identification().trim().matches("^\\d+$")) {
            throw new BusinessRuleException("La cédula debe contener únicamente números");
        }
        if (request.allowedRole() == null) {
            throw new BusinessRuleException("El rol es requerido");
        }
        if (request.allowedRole() != Role.ADMINISTRATOR
                && request.allowedRole() != Role.REFEREE
                && request.allowedRole() != Role.ORGANIZER) {
            throw new BusinessRuleException("Solo se pueden registrar cédulas para roles ADMINISTRATOR, REFEREE y ORGANIZER");
        }
        if (allowedIdentificationRepository.existsByIdentification(request.identification().trim())) {
            throw new BusinessRuleException("La cédula " + request.identification().trim() + " ya está registrada");
        }

        AllowedIdentificationEntity entity = AllowedIdentificationEntity.builder()
                .identification(request.identification().trim())
                .allowedRole(request.allowedRole())
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(allowedIdentificationRepository.save(entity));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Remove allowed identification",
            description = "Removes a whitelisted cédula. Cannot remove entries that have already been used. Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        AllowedIdentificationEntity entry = allowedIdentificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allowed identification not found"));
        if (entry.isUsed()) {
            throw new BusinessRuleException("No se puede eliminar una cédula que ya fue utilizada en un registro");
        }
        allowedIdentificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record AllowedIdRequest(String identification, Role allowedRole) {
    }
}
