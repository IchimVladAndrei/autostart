package com.autodrive.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleVehicleNotFoundException(VehicleNotFoundException e, WebRequest request) {
        log.warn("Vehicle not found: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VehicleAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleVehicleAlreadyExists(VehicleAlreadyExistsException e, WebRequest request) {
        log.warn("Vehicle already exists: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidVehicleException.class)
    public ResponseEntity<ErrorDetails> handleInvalidVehicle(InvalidVehicleException e, WebRequest request) {
        log.warn("Invalid vehicle data: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidVinException.class)
    public ResponseEntity<ErrorDetails> handleInvalidVinException(InvalidVinException e, WebRequest request) {
        log.warn("Invalid VIN: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {
        log.warn("Validation failed: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();

        e
                .getBindingResult()
                .getAllErrors()
                .forEach((err) -> {
                    String fieldName = ((FieldError) err).getField();
                    String errMessage = err.getDefaultMessage();
                    errors.put(fieldName, errMessage);
                });
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Validation Failed",
                request.getDescription(false),
                errors
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(java.lang.Exception e, WebRequest request) {
        log.error("Critical internal server error occurred:{}", e.getMessage(), e);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "INTERNAL SERVER ERROR",
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        log.error("Illegal argument: {}", e.getMessage(), e);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        log.warn("Entity not found: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleBrandNotFound(BrandNotFoundException e, WebRequest request) {
        log.warn("Brand not found: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExtraOptionNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleExtraOptionsNotFound(ExtraOptionNotFoundException e, WebRequest request) {
        log.warn("Extra option not found: {}", e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false),
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


}
