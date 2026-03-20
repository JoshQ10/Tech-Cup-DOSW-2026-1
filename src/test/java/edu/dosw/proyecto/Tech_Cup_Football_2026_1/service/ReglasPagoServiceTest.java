package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.EstadoPago;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReglasPagoServiceTest {

    private final ReglasPagoService reglasPagoService = new ReglasPagoService();

    @Test
    void validaTransicionesPago() {
        assertDoesNotThrow(() -> reglasPagoService.validarFlujoEstados(EstadoPago.PENDIENTE, EstadoPago.EN_REVISION));
        assertDoesNotThrow(() -> reglasPagoService.validarFlujoEstados(EstadoPago.EN_REVISION, EstadoPago.APROBADO));
        assertThrows(DatosInvalidosException.class, () -> reglasPagoService.validarFlujoEstados(EstadoPago.PENDIENTE, EstadoPago.APROBADO));
    }

    @Test
    void validaParticipacionSoloConPagoAprobado() {
        assertDoesNotThrow(() -> reglasPagoService.validarEquipoHabilitado(EstadoPago.APROBADO));
        assertThrows(DatosInvalidosException.class, () -> reglasPagoService.validarEquipoHabilitado(EstadoPago.EN_REVISION));
    }
}

