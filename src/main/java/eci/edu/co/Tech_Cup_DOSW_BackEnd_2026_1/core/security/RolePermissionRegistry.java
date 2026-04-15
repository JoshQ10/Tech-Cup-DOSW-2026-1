package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Permission;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class RolePermissionRegistry {

    private final Map<Role, Set<Permission>> permissionsByRole;

    public RolePermissionRegistry() {
        EnumMap<Role, Set<Permission>> map = new EnumMap<>(Role.class);

        map.put(Role.ADMINISTRATOR, EnumSet.allOf(Permission.class));

        map.put(Role.ORGANIZER, EnumSet.of(
                Permission.AUTH_REGISTER,
                Permission.AUTH_LOGIN,
                Permission.AUTH_LOGOUT,
                Permission.AUTH_REFRESH_TOKEN,
                Permission.AUTH_RECOVER_PASSWORD,
                Permission.AUTH_VERIFY_EMAIL,
                Permission.USER_READ,
                Permission.USER_UPDATE,
                Permission.PROFILE_READ_OWN,
                Permission.PROFILE_UPDATE_OWN,
                Permission.PROFILE_UPLOAD_AVATAR,
                Permission.PROFILE_UPLOAD_FULL_PHOTO,
                Permission.USER_STATS_READ,
                Permission.TOURNAMENT_CREATE,
                Permission.TOURNAMENT_UPDATE,
                Permission.TOURNAMENT_DELETE,
                Permission.TOURNAMENT_READ,
                Permission.TOURNAMENT_CONFIRM_RULES,
                Permission.TOURNAMENT_VIEW_BRACKET,
                Permission.TOURNAMENT_VIEW_STANDINGS,
                Permission.TEAM_CREATE,
                Permission.TEAM_UPDATE,
                Permission.TEAM_DELETE,
                Permission.TEAM_READ,
                Permission.TEAM_MANAGE_PLAYERS,
                Permission.TEAM_VIEW_OWN,
                Permission.INVITATION_READ,
                Permission.INVITATION_SEND,
                Permission.INVITATION_ACCEPT,
                Permission.INVITATION_REJECT,
                Permission.INVITATION_DELETE,
                Permission.MATCH_CREATE,
                Permission.MATCH_UPDATE,
                Permission.MATCH_DELETE,
                Permission.MATCH_READ,
                Permission.MATCH_CHANGE_STATUS,
                Permission.RESULT_REGISTER,
                Permission.POSSESSION_REGISTER,
                Permission.RESULT_READ,
                Permission.PAYMENT_REGISTER,
                Permission.PAYMENT_READ,
                Permission.PAYMENT_UPDATE,
                Permission.PAYMENT_DELETE,
                Permission.SANCTION_CREATE,
                Permission.SANCTION_UPDATE,
                Permission.SANCTION_READ,
                Permission.REPORT_READ_STATS,
                Permission.REPORT_READ_TOP_SCORERS,
                Permission.REPORT_READ_MATCH_HISTORY,
                Permission.REPORT_READ_PERFORMANCE,
                Permission.NOTIFICATION_READ,
                Permission.NOTIFICATION_READ_COUNT,
                Permission.NOTIFICATION_SEND_EMAIL,
                Permission.AUDIT_READ,
                Permission.SYSTEM_MANAGE,
                Permission.BACKEND_VALIDATE));

        map.put(Role.CAPTAIN, EnumSet.of(
                Permission.AUTH_REGISTER,
                Permission.AUTH_LOGIN,
                Permission.AUTH_LOGOUT,
                Permission.AUTH_REFRESH_TOKEN,
                Permission.AUTH_RECOVER_PASSWORD,
                Permission.AUTH_VERIFY_EMAIL,
                Permission.USER_READ,
                Permission.USER_UPDATE,
                Permission.PROFILE_READ_OWN,
                Permission.PROFILE_UPDATE_OWN,
                Permission.PROFILE_UPLOAD_AVATAR,
                Permission.PROFILE_UPLOAD_FULL_PHOTO,
                Permission.USER_STATS_READ,
                Permission.TOURNAMENT_READ,
                Permission.TOURNAMENT_CONFIRM_RULES,
                Permission.TOURNAMENT_VIEW_BRACKET,
                Permission.TOURNAMENT_VIEW_STANDINGS,
                Permission.TEAM_CREATE,
                Permission.TEAM_UPDATE,
                Permission.TEAM_READ,
                Permission.TEAM_MANAGE_PLAYERS,
                Permission.TEAM_VIEW_OWN,
                Permission.INVITATION_READ,
                Permission.INVITATION_SEND,
                Permission.INVITATION_ACCEPT,
                Permission.INVITATION_REJECT,
                Permission.MATCH_READ,
                Permission.RESULT_READ,
                Permission.LINEUP_CREATE,
                Permission.LINEUP_UPDATE,
                Permission.LINEUP_READ,
                Permission.PAYMENT_READ,
                Permission.SANCTION_READ,
                Permission.REPORT_READ_STATS,
                Permission.REPORT_READ_TOP_SCORERS,
                Permission.REPORT_READ_MATCH_HISTORY,
                Permission.REPORT_READ_PERFORMANCE,
                Permission.NOTIFICATION_READ,
                Permission.NOTIFICATION_READ_COUNT,
                Permission.BACKEND_VALIDATE));

        map.put(Role.REFEREE, EnumSet.of(
                Permission.AUTH_REGISTER,
                Permission.AUTH_LOGIN,
                Permission.AUTH_LOGOUT,
                Permission.AUTH_REFRESH_TOKEN,
                Permission.AUTH_RECOVER_PASSWORD,
                Permission.AUTH_VERIFY_EMAIL,
                Permission.USER_READ,
                Permission.PROFILE_READ_OWN,
                Permission.PROFILE_UPDATE_OWN,
                Permission.PROFILE_UPLOAD_AVATAR,
                Permission.PROFILE_UPLOAD_FULL_PHOTO,
                Permission.USER_STATS_READ,
                Permission.TOURNAMENT_READ,
                Permission.TOURNAMENT_CONFIRM_RULES,
                Permission.TOURNAMENT_VIEW_BRACKET,
                Permission.TOURNAMENT_VIEW_STANDINGS,
                Permission.TEAM_READ,
                Permission.TEAM_VIEW_OWN,
                Permission.INVITATION_READ,
                Permission.MATCH_READ,
                Permission.MATCH_CHANGE_STATUS,
                Permission.RESULT_REGISTER,
                Permission.GOAL_REGISTER,
                Permission.CARD_REGISTER,
                Permission.POSSESSION_REGISTER,
                Permission.RESULT_READ,
                Permission.LINEUP_READ,
                Permission.PAYMENT_READ,
                Permission.SANCTION_CREATE,
                Permission.SANCTION_UPDATE,
                Permission.SANCTION_READ,
                Permission.REPORT_READ_STATS,
                Permission.REPORT_READ_TOP_SCORERS,
                Permission.REPORT_READ_MATCH_HISTORY,
                Permission.REPORT_READ_PERFORMANCE,
                Permission.NOTIFICATION_READ,
                Permission.NOTIFICATION_READ_COUNT,
                Permission.BACKEND_VALIDATE));

        map.put(Role.PLAYER, EnumSet.of(
                Permission.AUTH_REGISTER,
                Permission.AUTH_LOGIN,
                Permission.AUTH_LOGOUT,
                Permission.AUTH_REFRESH_TOKEN,
                Permission.AUTH_RECOVER_PASSWORD,
                Permission.AUTH_VERIFY_EMAIL,
                Permission.USER_READ,
                Permission.USER_UPDATE,
                Permission.PROFILE_READ_OWN,
                Permission.PROFILE_UPDATE_OWN,
                Permission.PROFILE_UPLOAD_AVATAR,
                Permission.PROFILE_UPLOAD_FULL_PHOTO,
                Permission.USER_STATS_READ,
                Permission.TOURNAMENT_READ,
                Permission.TOURNAMENT_CONFIRM_RULES,
                Permission.TOURNAMENT_VIEW_BRACKET,
                Permission.TOURNAMENT_VIEW_STANDINGS,
                Permission.TEAM_READ,
                Permission.TEAM_VIEW_OWN,
                Permission.INVITATION_READ,
                Permission.INVITATION_ACCEPT,
                Permission.INVITATION_REJECT,
                Permission.MATCH_READ,
                Permission.RESULT_READ,
                Permission.LINEUP_READ,
                Permission.PAYMENT_REGISTER,
                Permission.PAYMENT_READ,
                Permission.SANCTION_READ,
                Permission.REPORT_READ_STATS,
                Permission.REPORT_READ_TOP_SCORERS,
                Permission.REPORT_READ_MATCH_HISTORY,
                Permission.REPORT_READ_PERFORMANCE,
                Permission.NOTIFICATION_READ,
                Permission.NOTIFICATION_READ_COUNT,
                Permission.BACKEND_VALIDATE));

        permissionsByRole = Collections.unmodifiableMap(map);
    }

    public Set<Permission> getPermissions(Role role) {
        return permissionsByRole.getOrDefault(role, EnumSet.noneOf(Permission.class));
    }

    public boolean hasPermission(Role role, Permission permission) {
        return getPermissions(role).contains(permission);
    }
}
