package edu.dosw.proyecto.Tech_Cup_Football_2026_1.validator;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void testEmailValido() {
        assertTrue(EmailValidator.validar("usuario@example.com"), "Email válido debería pasar validación");
        assertTrue(EmailValidator.validar("test.email@dominio.co"), "Email con punto debería ser válido");
        assertTrue(EmailValidator.validar("nombre+tag@example.org"), "Email con + debería ser válido");
    }

    @Test
    void testEmailInvalido() {
        assertFalse(EmailValidator.validar("emailSinArroba"), "Email sin @ debería ser inválido");
        assertFalse(EmailValidator.validar("email@.com"), "Email sin dominio debería ser inválido");
        assertFalse(EmailValidator.validar("email@dominio"), "Email sin extensión debería ser inválido");
        assertFalse(EmailValidator.validar("email @dominio.com"), "Email con espacio debería ser inválido");
    }

    @Test
    void testEmailNulo() {
        assertFalse(EmailValidator.validar(null), "Email nulo debería ser inválido");
    }

    @Test
    void testEmailVacio() {
        assertFalse(EmailValidator.validar(""), "Email vacío debería ser inválido");
    }

    @Test
    void testReglaCorreoInstitucional() {
        assertTrue(EmailValidator.cumpleReglaPorTipo("ana@escuelaing.edu.co", TipoParticipante.ESTUDIANTE));
        assertFalse(EmailValidator.cumpleReglaPorTipo("ana@gmail.com", TipoParticipante.PROFESOR));
    }

    @Test
    void testReglaCorreoExterno() {
        assertTrue(EmailValidator.cumpleReglaPorTipo("externo@gmail.com", TipoParticipante.EXTERNO));
        assertFalse(EmailValidator.cumpleReglaPorTipo("externo@escuelaing.edu.co", TipoParticipante.EXTERNO));
    }
}
