package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import jakarta.persistence.Entity;
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lineup_players")
public class LineupPlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lineup_id")
    private LineupEntity lineup;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private UserEntity player;

    private Integer positionX;

    private Integer positionY;

    @Builder.Default
    private boolean starter = false;
}
