package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Role role;
    private UserType userType;
    private Program program;
    private String identification;
    private String relationshipType;
    private String relationshipDescription;
    private String avatarUrl;
    private boolean active;
    private LocalDateTime createdAt;

    public boolean isActive() {
        return active;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
