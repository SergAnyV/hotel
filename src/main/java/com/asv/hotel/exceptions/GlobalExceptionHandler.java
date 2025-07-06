package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.DataNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleDataNotFound(DataNotFoundException ex) {
        ErrorMessage response = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleDataAlreadyExists(DataAlreadyExistsException ex) {
        ErrorMessage response = new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataAccessException ex) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAllExceptions(Exception ex,String message) {
        ErrorMessage response = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}