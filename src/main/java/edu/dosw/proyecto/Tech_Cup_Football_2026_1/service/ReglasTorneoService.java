package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.EstadoTorneo;
import org.springframework.stereotype.Service;

@Service
public class ReglasTorneoService {

    public void validarCambioEstado(EstadoTorneo actual, EstadoTorneo siguiente) {
        if (actual == null || siguiente == null) {
            throw new DatosInvalidosException("El estado del torneo no puede ser nulo");
        }

        if (actual == EstadoTorneo.BORRADOR && siguiente == EstadoTorneo.ACTIVO) {
            return;
        }
        if (actual == EstadoTorneo.ACTIVO && siguiente == EstadoTorneo.EN_PROGRESO) {
            return;
        }
        if (actual == EstadoTorneo.EN_PROGRESO && siguiente == EstadoTorneo.FINALIZADO) {
            return;
        }
        if (actual == siguiente) {
            return;
        }

        throw new DatosInvalidosException("Transicion de estado del torneo invalida");
    }
}

