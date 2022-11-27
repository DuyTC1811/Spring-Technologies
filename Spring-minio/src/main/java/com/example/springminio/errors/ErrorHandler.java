package com.example.springminio.errors;

import com.example.springminio.entitys.ResponseError;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class,})
    public ResponseEntity<ResponseError> handleCustomBadRequestException(Exception exception, HttpServletRequest request) {
        ResponseError response = errorDetails(exception.getMessage(), exception, BAD_REQUEST, request);
        return ResponseEntity.status(BAD_REQUEST).contentType(getMediaType()).body(response);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({MinioException.class, InvocationTargetException.class})
    public ResponseEntity<ResponseError> handleMinioException(Exception exception, HttpServletRequest request) {
        ResponseError response = errorDetails("File Not Found !", exception, NOT_FOUND, request);
        return ResponseEntity.status(NOT_FOUND).contentType(getMediaType()).body(response);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IllegalStateException.class, IOException.class})
    ResponseEntity<ResponseError> handleException(Exception exception, HttpServletRequest request) {
        ResponseError response = errorDetails(exception.getMessage(), exception, INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(getMediaType()).body(response);
    }

    private ResponseError errorDetails(String message, Exception exception, HttpStatus httpStatus, HttpServletRequest request) {
        var errorDetail = ResponseError.builder()
                .message(message)
                .status(httpStatus.value())
                .timestamp(new Date())
                .error(httpStatus.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length())).build();

        log.error(exception.getMessage());
        return errorDetail;
    }

    private MediaType getMediaType() {
        switch (MediaTypeInfo.getCurrentMediaType()) {
            case "hal":
            case "json":
                return MediaType.APPLICATION_JSON;
            case "xml":
                return MediaType.APPLICATION_XML;
            default:
                return MediaType.ALL;
        }
    }
}
