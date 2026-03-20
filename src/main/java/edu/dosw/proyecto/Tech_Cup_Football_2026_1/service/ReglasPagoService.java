package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.EstadoPago;
import org.springframework.stereotype.Service;

@Service
public class ReglasPagoService {

    public void validarFlujoEstados(EstadoPago actual, EstadoPago siguiente) {
        if (actual == null || siguiente == null) {
            throw new DatosInvalidosException("El estado de pago no puede ser nulo");
        }

        if (actual == EstadoPago.PENDIENTE && (siguiente == EstadoPago.EN_REVISION || siguiente == EstadoPago.RECHAZADO)) {
            return;
        }

        if (actual == EstadoPago.EN_REVISION && (siguiente == EstadoPago.APROBADO || siguiente == EstadoPago.RECHAZADO)) {
            return;
        }

        if (actual == EstadoPago.RECHAZADO && siguiente == EstadoPago.EN_REVISION) {
            return;
        }

        if (actual == siguiente) {
            return;
        }

        throw new DatosInvalidosException("Transicion de estado de pago invalida");
    }

    public void validarEquipoHabilitado(EstadoPago estadoPagoEquipo) {
        if (estadoPagoEquipo != EstadoPago.APROBADO) {
            throw new DatosInvalidosException("Solo equipos con pago aprobado pueden participar");
        }
    }
}

