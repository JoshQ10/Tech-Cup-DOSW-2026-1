package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el perfil completo del usuario")
public class UserCompleteProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatarUrl;
    private Role role;
    private UserType userType;
    private String relationshipType;
    private String relationshipDescription;
    private SportsProfileInfo sportsProfile;
    private UserStatsInfo statistics;
    private CurrentTeamInfo currentTeam;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Informacion deportiva del usuario")
    public static class SportsProfileInfo {
        private Long profileId;
        private Position primaryPosition;
        private Position secondaryPosition;
        private Integer jerseyNumber;
        private DominantFoot dominantFoot;
        private Boolean available;
        private String photoUrl;
        private String fullPhotoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Estadisticas deportivas del usuario")
    public static class UserStatsInfo {
        private long goals;
        private long assists;
        private long matchesPlayed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Equipo actual del usuario")
    public static class CurrentTeamInfo {
        private Long teamId;
        private String name;
        private Long tournamentId;
    }
}