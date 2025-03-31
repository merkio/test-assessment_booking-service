package com.statista.code.challenge.api.booking.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.statista.code.challenge.domain.exception.NotFoundEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@Slf4j
@ControllerAdvice
public class BookingControllerExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            JsonParseException.class,
            DateTimeParseException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        log.error("Invalid request data", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .error(ex.getMessage())
                                .code(HttpStatus.BAD_REQUEST.value())
                                .build()
                );
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundEntityException(Exception ex) {
        log.error("Data not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .error(ex.getMessage())
                                .code(HttpStatus.NOT_FOUND.value())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .error(ex.getMessage())
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .build()
                );
    }
}
