package br.com.rafaeluz.taskmanager.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Custommização da Exception
 */
@ControllerAdvice
public class ExeptionHandlerController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());

    }
}
