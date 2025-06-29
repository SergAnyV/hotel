package com.asv.hotel.exceptions;

import com.asv.hotel.exceptions.rooms.ErrorAnswer;
import com.asv.hotel.exceptions.rooms.RoomAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.RoomNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorAnswer> handleRoomNotFound(RoomNotFoundException ex) {
        ErrorAnswer response = new ErrorAnswer("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorAnswer> handleRoomAlreadyExists(RoomAlreadyExistsException ex) {
        ErrorAnswer response = new ErrorAnswer("CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorAnswer> handleDataIntegrityViolation(DataAccessException ex) {
        ErrorAnswer response = new ErrorAnswer("BAD_REQUEST", "нарушение целостности базы данных");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        return ResponseEntity.badRequest().body(errors);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorAnswer> handleAllExceptions(Exception ex) {
        ErrorAnswer response = new ErrorAnswer(
                "INTERNAL_SERVER_ERROR",
                "Ошибка сервера");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}