package org.example.springsecurity.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.springsecurity.enums.EException;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private int code;
    private String message;

    public BaseException(EException exception) {
        this.code = exception.getCode();
        this.message = exception.getMessage();
    }
}
