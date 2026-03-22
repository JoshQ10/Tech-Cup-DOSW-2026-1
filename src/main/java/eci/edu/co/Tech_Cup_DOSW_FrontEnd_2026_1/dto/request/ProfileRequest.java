package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private Position position;
    private int jerseyNumber;
    private String photoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;
}
