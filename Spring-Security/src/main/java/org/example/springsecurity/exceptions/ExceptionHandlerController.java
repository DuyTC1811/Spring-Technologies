package org.example.springsecurity.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class ExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(BaseException exception) {
        return handlerBaseResponse(exception.getCode(), exception.getMessage(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(MethodArgumentNotValidException exception) {
        String message = "Invalid field";
        StringBuilder details = new StringBuilder();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String fieldErrorLogMessage = "filed -> " + fieldName + ": " + errorMessage;
            details.append(fieldErrorLogMessage).append(", ");
        });
        return handlerBaseResponse(HttpStatus.BAD_REQUEST.value(), message, details.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleSecurityException(Exception exception) {

        if (exception instanceof BadCredentialsException) {
            return handlerResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), "The username or password is incorrect");
        }

        if (exception instanceof AccountStatusException) {
            return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The JWT token has expired");
        }

        if (exception instanceof MalformedJwtException) {
            return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "Invalid compact JWT string");
        }
        LOGGER.error("[ EXCEPTION-CLASS :: {} ] {} ", exception.getClass().getSimpleName(), exception.getMessage());
        return handlerBaseResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error", "An error occurred");
    }

    private ResponseEntity<ExceptionResponse> handlerBaseResponse(int code, String detail, String description) {
        LOGGER.error("[ EXCEPTION ] - {}", detail);
        ExceptionResponse response = new ExceptionResponse(code, detail, description);
        return ResponseEntity.status(code).body(response);
    }

    private ResponseEntity<ExceptionResponse> handlerResponse(int code, String detail, String description) {
        LOGGER.error("[ EXCEPTION-JWT ] - {}", detail);
        ExceptionResponse response = new ExceptionResponse(code, detail, description);
        return ResponseEntity.status(code).body(response);
    }
}
