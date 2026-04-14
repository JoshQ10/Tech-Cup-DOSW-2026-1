package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapPoint {
    private double x;
    private double y;
}
