package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.PaymentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;
    private Team team;
    private String receiptUrl;
    private PaymentStatus status;
    private User reviewedBy;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
