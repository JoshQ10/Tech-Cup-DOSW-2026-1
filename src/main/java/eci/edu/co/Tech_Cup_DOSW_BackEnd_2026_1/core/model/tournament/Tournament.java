package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int teamCount;
    private double costPerTeam;

    @Column(columnDefinition = "TEXT")
    private String rules;

    private LocalDate inscriptionCloseDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status;
}
