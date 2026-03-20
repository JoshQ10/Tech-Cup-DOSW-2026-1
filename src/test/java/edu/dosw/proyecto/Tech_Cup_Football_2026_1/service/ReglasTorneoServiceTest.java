package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.EstadoTorneo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReglasTorneoServiceTest {

    private final ReglasTorneoService reglasTorneoService = new ReglasTorneoService();

    @Test
    void validaTransicionesTorneo() {
        assertDoesNotThrow(() -> reglasTorneoService.validarCambioEstado(EstadoTorneo.BORRADOR, EstadoTorneo.ACTIVO));
        assertDoesNotThrow(() -> reglasTorneoService.validarCambioEstado(EstadoTorneo.ACTIVO, EstadoTorneo.EN_PROGRESO));
        assertDoesNotThrow(() -> reglasTorneoService.validarCambioEstado(EstadoTorneo.EN_PROGRESO, EstadoTorneo.FINALIZADO));

        assertThrows(DatosInvalidosException.class,
                () -> reglasTorneoService.validarCambioEstado(EstadoTorneo.BORRADOR, EstadoTorneo.FINALIZADO));
    }
}

