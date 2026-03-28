package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
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
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private User referee;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    private LocalDate matchDate;

    private LocalTime matchTime;

    @Enumerated(EnumType.STRING)
    private MatchPhase phase;

    private Integer homeScore;

    private Integer awayScore;

    @Builder.Default
    private boolean played = false;
}
