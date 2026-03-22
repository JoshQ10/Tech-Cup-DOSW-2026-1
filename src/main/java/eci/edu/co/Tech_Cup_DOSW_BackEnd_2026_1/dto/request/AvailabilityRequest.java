package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityRequest {
    private boolean available;
}
