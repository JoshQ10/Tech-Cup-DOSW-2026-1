package edu.dosw.proyecto.Tech_Cup_Football_2026_1.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordEncoderUtil {

    private PasswordEncoderUtil() {
    }

    public static String encode(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("La contraseña no puede ser nula");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No fue posible codificar la contraseña", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encode(rawPassword).equals(encodedPassword);
    }
}

