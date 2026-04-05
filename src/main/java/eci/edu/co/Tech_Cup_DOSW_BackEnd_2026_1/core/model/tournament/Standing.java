package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
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
@Table(name = "standings")
public class Standing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder.Default
    private int played = 0;

    @Builder.Default
    private int won = 0;

    @Builder.Default
    private int drawn = 0;

    @Builder.Default
    private int lost = 0;

    @Builder.Default
    private int goalsFor = 0;

    @Builder.Default
    private int goalsAgainst = 0;

    @Builder.Default
    private int goalDifference = 0;

    @Builder.Default
    private int points = 0;
}
