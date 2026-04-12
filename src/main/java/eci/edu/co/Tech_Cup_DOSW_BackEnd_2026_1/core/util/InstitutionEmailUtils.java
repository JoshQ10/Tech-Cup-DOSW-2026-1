package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class InstitutionEmailUtils {

    private static final Pattern CAPTAIN_EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9]+(?:\\.[A-Za-z0-9]+)*(?:-[A-Za-z0-9])?@(escuelaing\\.edu\\.co|mail\\.escuelaing\\.edu\\.co)$",
            Pattern.CASE_INSENSITIVE);

    private InstitutionEmailUtils() {
    }

    public static boolean isEscuelaingEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String normalized = email.toLowerCase(Locale.ROOT);
        return normalized.endsWith("@escuelaing.edu.co") || normalized.endsWith("@mail.escuelaing.edu.co");
    }

    public static boolean isValidCaptainInstitutionalEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return CAPTAIN_EMAIL_PATTERN.matcher(email).matches();
    }
}
