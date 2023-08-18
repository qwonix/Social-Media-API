package ru.qwonix.test.social.media.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.qwonix.test.social.media.api.dto.ErrorMessage;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        var errorMessages = ex.getFieldErrors().stream()
                .map(error -> new ErrorMessage(error.getField(), error.getDefaultMessage())).toList();

        return ResponseEntity.badRequest().body(
                new ErrorResponse(errorMessages)
        );
    }
}