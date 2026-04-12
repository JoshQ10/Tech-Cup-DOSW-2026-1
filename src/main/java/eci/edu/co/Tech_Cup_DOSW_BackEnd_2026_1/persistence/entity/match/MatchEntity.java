package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
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

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private TournamentEntity tournament;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private TeamEntity homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private TeamEntity awayTeam;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private UserEntity referee;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private CourtEntity court;

    private LocalDate matchDate;

    private LocalTime matchTime;

    @Enumerated(EnumType.STRING)
    private MatchPhase phase;

    private Integer homeScore;

    private Integer awayScore;

    @Builder.Default
    private boolean played = false;
}
