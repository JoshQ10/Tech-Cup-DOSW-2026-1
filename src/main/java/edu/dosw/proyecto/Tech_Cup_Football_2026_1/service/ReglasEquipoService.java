package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.DatosInvalidosException;
import org.springframework.stereotype.Service;

@Service
public class ReglasEquipoService {

    public void validarCantidadJugadores(int totalJugadores) {
        if (totalJugadores < 7) {
            throw new DatosInvalidosException("Un equipo debe tener minimo 7 jugadores");
        }
        if (totalJugadores > 12) {
            throw new DatosInvalidosException("Un equipo puede tener maximo 12 jugadores");
        }
    }

    public void validarJugadorUnicoPorTorneo(boolean yaPerteneceAEquipo) {
        if (yaPerteneceAEquipo) {
            throw new DatosInvalidosException("Un jugador no puede pertenecer a mas de un equipo por torneo");
        }
    }

    public void validarMayoriaIngenieria(int totalJugadores, int jugadoresIngenieria) {
        validarCantidadJugadores(totalJugadores);
        if (jugadoresIngenieria <= totalJugadores / 2) {
            throw new DatosInvalidosException("Mas de la mitad del equipo debe pertenecer a programas de ingenieria");
        }
    }

    public void validarBloqueoNomina(boolean torneoEnProgreso, boolean intentaCambiarJugadoresIniciales) {
        if (torneoEnProgreso && intentaCambiarJugadoresIniciales) {
            throw new DatosInvalidosException("Los 12 jugadores iniciales deben terminar el torneo");
        }
    }
}

