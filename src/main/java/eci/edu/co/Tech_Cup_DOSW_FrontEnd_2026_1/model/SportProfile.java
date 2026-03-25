package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Position position;
    private int jerseyNumber;
    private String photoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;
}
