package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_invitations")
public class TeamInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private User player;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private LocalDateTime sentAt;

    private LocalDateTime respondedAt;
}
