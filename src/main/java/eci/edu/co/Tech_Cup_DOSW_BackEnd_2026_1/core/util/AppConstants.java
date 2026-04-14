package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util;

/**
 * Constantes de mensajes de error y respuesta para la aplicación.
 * Utilizadas para evitar duplicación de strings y mejorar mantenibilidad.
 */
public class AppConstants {

    // Error messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_PROFILE_NOT_FOUND = "Profile not found";
    public static final String ERROR_TEAM_NOT_FOUND = "Team not found";
    public static final String ERROR_TOURNAMENT_NOT_FOUND = "Tournament not found";
    public static final String ERROR_MATCH_NOT_FOUND = "Match not found";
    public static final String ERROR_INVITATION_NOT_FOUND = "Invitation not found";

    // Team configuration
    public static final int MAX_TEAM_PLAYERS = 15;

    // Success messages
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_REFRESH = "refresh";
    public static final String MSG_CREATED = "created";
    public static final String MSG_UPDATED = "updated";
    public static final String MSG_DELETED = "deleted";

    // JWT & Auth
    public static final String TOKEN_TYPE = "Bearer";
    public static final String BEARER_PREFIX = "Bearer ";

    // General messages
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ACCOUNT_INACTIVE = "User account is inactive";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";

    // Avoid instantiation
    private AppConstants() {
        throw new AssertionError("Cannot instantiate AppConstants");
    }
}
