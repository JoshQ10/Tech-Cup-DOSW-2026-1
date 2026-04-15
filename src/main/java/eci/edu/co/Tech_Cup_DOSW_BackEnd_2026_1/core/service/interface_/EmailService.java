package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;

public interface EmailService {

    /**
     * Envía email de verificación al usuario después del registro
     * 
     * @param email     Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName  Apellido del usuario
     * @param token     Token de verificación
     * @param userType  Tipo de usuario (INTERNAL o EXTERNAL)
     * @param role      Rol del usuario (PLAYER, CAPTAIN, etc.)
     */
    void sendVerificationEmail(String email, String firstName, String lastName, String token, UserType userType,
            Role role);

    /**
     * Reenvía email de verificación
     * 
     * @param email     Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName  Apellido del usuario
     * @param token     Token de verificación
     * @param userType  Tipo de usuario (INTERNAL o EXTERNAL)
     * @param role      Rol del usuario (PLAYER, CAPTAIN, etc.)
     */
    void sendVerificationEmailResend(String email, String firstName, String lastName, String token, UserType userType,
            Role role);

    /**
     * Envía email de bienvenida después de verificar cuenta
     * 
     * @param email     Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName  Apellido del usuario
     */
    void sendWelcomeEmail(String email, String firstName, String lastName);

    /**
     * Envia email para recuperacion de contrasena
     * 
     * @param email     Email del usuario
     * @param firstName Nombre del usuario
     * @param lastName  Apellido del usuario
     * @param token     Token de recuperacion
     */
    void sendPasswordResetEmail(String email, String firstName, String lastName, String token);
}
