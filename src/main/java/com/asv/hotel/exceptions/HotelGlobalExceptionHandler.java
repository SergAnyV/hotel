package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class HotelGlobalExceptionHandler {

    @ExceptionHandler(HotelMainException.class)
    public ResponseEntity<ErrorMessage> handleHotelMainException(HotelMainException ex) {
        ErrorMessage errorMessage = ex.getErrorMessage();
        if (ex.getHttpStatus().is4xxClientError()) {
            log.warn("Client error [{}]: {}", ex.getHttpStatus(), ex.getMessage());
        } else {
            log.error("Server error [{}]: {}", ex.getHttpStatus(), ex.getMessage());
        }
        return ResponseEntity.status(ex.getHttpStatus()).body(errorMessage);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> handleDataAccessException(DataAccessException ex) {
        return response500Error(ex, e -> "Database access error");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.FORBIDDEN,
                "Access denied: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Unauthorized : {}", ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", errorMessage);

        ErrorMessage error = new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "Validation error: " + errorMessage
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        return response500Error(ex, e -> "Unexpected Internal Error ");
    }

    private ResponseEntity<ErrorMessage> response500Error(Exception ex, Function<Exception, String> function) {
        String errorMessage = function.apply(ex);
        log.error("{}: {}", errorMessage, ex.getMessage(), ex);
        ErrorMessage eMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorMessage
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eMessage);
    }

}