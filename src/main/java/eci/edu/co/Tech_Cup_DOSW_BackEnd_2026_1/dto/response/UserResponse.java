package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Role role;
    private boolean active;
}
