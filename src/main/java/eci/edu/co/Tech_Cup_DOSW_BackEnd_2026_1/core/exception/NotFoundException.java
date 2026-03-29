package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
