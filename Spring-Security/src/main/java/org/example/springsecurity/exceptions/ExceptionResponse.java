package org.example.springsecurity.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ExceptionResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer code;
    private String message;

    public ExceptionResponse(int code,String message) {
        this.code = code;
        this.message = message;
    }
}
