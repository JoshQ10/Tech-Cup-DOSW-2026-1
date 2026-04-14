package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
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
public class SportProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "secondary_position")
    private Position secondaryPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "dominant_foot")
    private DominantFoot dominantFoot;

    @Enumerated(EnumType.STRING)
    private Program program;

    private int jerseyNumber;
    private String photoUrl;
    @Column(name = "full_photo_url")
    private String fullPhotoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;

    @Column(name = "last_availability_change")
    private LocalDateTime lastAvailabilityChange;

    @Column(name = "availability_change_reason")
    private String availabilityChangeReason;

    public boolean isAvailable() {
        return available;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
