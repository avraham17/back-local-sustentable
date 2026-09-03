package edu.co.sena.worksite.exceptions;

import edu.co.sena.worksite.dtos.ErrorResponseDto;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(
            new ErrorResponseDto(ex.getMessage(),404),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex){
        return new ResponseEntity<>(
            new ErrorResponseDto(ex.getMessage(),400),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex){
        return new ResponseEntity<>(
            new ErrorResponseDto("Error de validación",400),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex){
        return new ResponseEntity<>(
            new ErrorResponseDto("Error interno del servidor",500),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
