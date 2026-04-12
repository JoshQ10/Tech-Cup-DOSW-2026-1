package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
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
public class TeamInvitation {

    private Long id;
    private Team team;
    private User player;
    private InvitationStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;
}
