package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;

public interface EmailService {

    /**
     * Envía email de verificación al usuario después del registro
     * @param email Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     * @param token Token de verificación
     * @param userType Tipo de usuario (INTERNAL o EXTERNAL)
     */
    void sendVerificationEmail(String email, String firstName, String lastName, String token, UserType userType);

    /**
     * Reenvía email de verificación
     * @param email Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     * @param token Token de verificación
     * @param userType Tipo de usuario (INTERNAL o EXTERNAL)
     */
    void sendVerificationEmailResend(String email, String firstName, String lastName, String token, UserType userType);

    /**
     * Envía email de bienvenida después de verificar cuenta
     * @param email Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     */
    void sendWelcomeEmail(String email, String firstName, String lastName);
}
