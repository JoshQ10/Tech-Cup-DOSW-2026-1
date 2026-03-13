package edu.dosw.proyecto.Tech_Cup_Football_2026_1.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.exception.*;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RespuestaError;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> manejarRecursoNoEncontrado(RecursoNoEncontradoException e) {
        RespuestaError error = new RespuestaError(404, "Recurso no encontrado", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<RespuestaError> manejarDatosInvalidos(DatosInvalidosException e) {
        RespuestaError error = new RespuestaError(400, "Datos inválidos", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorOperacionException.class)
    public ResponseEntity<RespuestaError> manejarErrorOperacion(ErrorOperacionException e) {
        RespuestaError error = new RespuestaError(500, "Error en la operación", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespuestaError> manejarArgumentoIlegal(IllegalArgumentException e) {
        RespuestaError error = new RespuestaError(400, "Argumento inválido", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
