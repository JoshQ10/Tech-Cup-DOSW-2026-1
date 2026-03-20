package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReglasEquipoServiceTest {

    private final ReglasEquipoService reglasEquipoService = new ReglasEquipoService();

    @Test
    void validaMinimoYMaximoJugadores() {
        assertDoesNotThrow(() -> reglasEquipoService.validarCantidadJugadores(7));
        assertDoesNotThrow(() -> reglasEquipoService.validarCantidadJugadores(12));
        assertThrows(DatosInvalidosException.class, () -> reglasEquipoService.validarCantidadJugadores(6));
        assertThrows(DatosInvalidosException.class, () -> reglasEquipoService.validarCantidadJugadores(13));
    }

    @Test
    void validaMayoriaIngenieria() {
        assertDoesNotThrow(() -> reglasEquipoService.validarMayoriaIngenieria(10, 6));
        assertThrows(DatosInvalidosException.class, () -> reglasEquipoService.validarMayoriaIngenieria(10, 5));
    }
}

