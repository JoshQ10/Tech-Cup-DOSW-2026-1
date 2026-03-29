package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
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

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sport_profiles")
public class SportProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private Program program;

    private int jerseyNumber;
    private String photoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;

    @Column(name = "last_availability_change")
    private LocalDateTime lastAvailabilityChange;

    @Column(name = "availability_change_reason")
    private String availabilityChangeReason;
}
