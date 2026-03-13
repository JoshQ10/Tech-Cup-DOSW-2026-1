package edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception;

public class ErrorOperacionException extends RuntimeException {
    public ErrorOperacionException(String mensaje) {
        super(mensaje);
    }

    public ErrorOperacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
