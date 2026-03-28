package org.example.springsecurity.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    // Custom Exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(BaseException exception) {
        return handlerBaseResponse(exception.getCode(), exception.getMessage(), exception.getMessage());
    }

    // Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidExceptionResponse> handlerResponse(MethodArgumentNotValidException exception) {
        String message = "Invalid field";
        Map<String, Object> errors = new LinkedHashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        });
        return handlerValidResponse(HttpStatus.BAD_REQUEST.value(), message, errors);
    }

    // Missing request param / path variable
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(MissingServletRequestParameterException exception) {
        return handlerBaseResponse(HttpStatus.BAD_REQUEST.value(), "Missing parameter: " + exception.getParameterName(), exception.getMessage());
    }

    // Request body sai format (JSON parse fail)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(HttpMessageNotReadableException exception) {
        return handlerBaseResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request body", exception.getMessage());
    }

    // Sai HTTP method (GET thay vì POST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(HttpRequestMethodNotSupportedException exception) {
        return handlerBaseResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not allowed: " + exception.getMethod(), exception.getMessage());
    }

    // Sai Content-Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(HttpMediaTypeNotSupportedException exception) {
        return handlerBaseResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Unsupported media type", exception.getMessage());
    }

    // 404 - Không tìm thấy resource
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(NoResourceFoundException exception) {
        return handlerBaseResponse(HttpStatus.NOT_FOUND.value(), "Resource not found", exception.getMessage());
    }

    // Type mismatch (ví dụ truyền String cho param kiểu Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(MethodArgumentTypeMismatchException exception) {
        String detail = "Invalid value for parameter: " + exception.getName();
        return handlerBaseResponse(HttpStatus.BAD_REQUEST.value(), detail, exception.getMessage());
    }

    // Security exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(BadCredentialsException exception) {
        return handlerResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), "The username or password is incorrect");
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(AccountStatusException exception) {
        return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The account is locked");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(AccessDeniedException exception) {
        return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "You are not authorized to access this resource");
    }

    // JWT exceptions
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(SignatureException exception) {
        return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The JWT signature is invalid");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(ExpiredJwtException exception) {
        return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "The JWT token has expired");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponse> handlerResponse(MalformedJwtException exception) {
        return handlerResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), "Invalid compact JWT string");
    }

    // Fallback - catch all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalException(Exception exception) {
        LOGGER.error("[ EXCEPTION-INTERNAL-SERVER ]", exception);
        return handlerBaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", exception.getMessage());
    }

    // Helper methods
    private ResponseEntity<ValidExceptionResponse> handlerValidResponse(int code, String detail, Map<String, Object> description) {
        LOGGER.error("[ EXCEPTION-VALID ] - {}", description);
        return ResponseEntity.status(code).body(new ValidExceptionResponse(code, detail, description));
    }

    private ResponseEntity<ExceptionResponse> handlerBaseResponse(int code, String detail, String description) {
        LOGGER.error("[ EXCEPTION ] - {}", detail);
        return ResponseEntity.status(code).body(new ExceptionResponse(code, detail, description));
    }

    private ResponseEntity<ExceptionResponse> handlerResponse(int code, String detail, String description) {
        LOGGER.error("[ EXCEPTION-JWT ] - {}", detail);
        return ResponseEntity.status(code).body(new ExceptionResponse(code, detail, description));
    }
}
