package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match_possessions")
public class MatchPossessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id", unique = true)
    private MatchEntity match;

    private int homePercentage;

    private int awayPercentage;

    @ElementCollection
    @CollectionTable(name = "match_possession_heatmap", joinColumns = @JoinColumn(name = "possession_id"))
    @Builder.Default
    private List<HeatmapPoint> heatmapPoints = new ArrayList<>();
}
