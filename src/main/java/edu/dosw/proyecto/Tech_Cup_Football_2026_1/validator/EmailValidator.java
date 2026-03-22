package edu.dosw.proyecto.Tech_Cup_Football_2026_1.validator;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final String GMAIL_DOMAIN = "@gmail.com";
    // Ajusta este dominio institucional segun la politica real de la universidad
    private static final String INSTITUTIONAL_DOMAIN = "@escuelaing.edu.co";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validar(String email) {
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    public static boolean esValido(String email) {
        return validar(email);
    }

    public static boolean esCorreoInstitucional(String email) {
        return validar(email) && email.toLowerCase().endsWith(INSTITUTIONAL_DOMAIN);
    }

    public static boolean esCorreoGmail(String email) {
        return validar(email) && email.toLowerCase().endsWith(GMAIL_DOMAIN);
    }

    public static boolean cumpleReglaPorTipo(String email, TipoParticipante tipoParticipante) {
        if (tipoParticipante == null) {
            return false;
        }

        if (tipoParticipante == TipoParticipante.EXTERNO) {
            return esCorreoGmail(email);
        }

        return esCorreoInstitucional(email);
    }
}
