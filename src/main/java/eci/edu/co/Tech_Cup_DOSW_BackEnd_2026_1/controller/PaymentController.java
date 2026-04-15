package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.PaymentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.PaymentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.PaymentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List payments", description = "Allowed roles: all authenticated")
    public ResponseEntity<List<Map<String, Object>>> list(@RequestParam(required = false) String status) {
        List<PaymentEntity> payments;
        if (status != null && !status.isBlank()) {
            payments = paymentRepository.findByStatus(PaymentStatus.valueOf(status));
        } else {
            payments = paymentRepository.findAll();
        }
        return ResponseEntity.ok(payments.stream().map(this::toMap).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment", description = "Allowed roles: all authenticated")
    public ResponseEntity<Map<String, Object>> getById(@Parameter(required = true) @PathVariable Long id) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return ResponseEntity.ok(toMap(payment));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER','PLAYER')")
    @Operation(summary = "Create payment", description = "Allowed roles: ADMINISTRATOR, ORGANIZER, PLAYER")
    public ResponseEntity<Map<String, Object>> create(@RequestBody PaymentWriteRequest request) {
        TeamEntity team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        PaymentEntity payment = PaymentEntity.builder()
                .team(team)
                .receiptUrl(request.receiptUrl())
                .status(PaymentStatus.PENDING)
                .comments(request.comments())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PaymentEntity saved = paymentRepository.save(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Update payment", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(required = true) @PathVariable Long id,
            @RequestBody PaymentWriteRequest request) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (request.receiptUrl() != null)
            payment.setReceiptUrl(request.receiptUrl());
        if (request.status() != null)
            payment.setStatus(request.status());
        if (request.comments() != null)
            payment.setComments(request.comments());
        if (request.reviewedById() != null) {
            UserEntity reviewer = userRepository.findById(request.reviewedById())
                    .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));
            payment.setReviewedBy(reviewer);
        }
        payment.setUpdatedAt(LocalDateTime.now());

        PaymentEntity updated = paymentRepository.save(payment);
        return ResponseEntity.ok(toMap(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Delete payment", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Void> delete(@Parameter(required = true) @PathVariable Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found");
        }
        paymentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toMap(PaymentEntity payment) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", payment.getId());
        data.put("teamId", payment.getTeam() != null ? payment.getTeam().getId() : null);
        data.put("receiptUrl", payment.getReceiptUrl());
        data.put("status", payment.getStatus());
        data.put("reviewedById", payment.getReviewedBy() != null ? payment.getReviewedBy().getId() : null);
        data.put("comments", payment.getComments());
        data.put("createdAt", payment.getCreatedAt());
        data.put("updatedAt", payment.getUpdatedAt());
        return data;
    }

    public record PaymentWriteRequest(
            Long teamId,
            String receiptUrl,
            PaymentStatus status,
            Long reviewedById,
            String comments) {
    }
}
