package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String shieldUrl;
    private String uniformColors;

    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "captain_id")
    private Long captainId;

    @ElementCollection
    @CollectionTable(name = "team_players", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "player_id")
    private List<Long> players;
}
