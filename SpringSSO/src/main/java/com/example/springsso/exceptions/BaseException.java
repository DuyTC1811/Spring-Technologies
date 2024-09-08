package com.example.springsso.exceptions;

import com.example.springsso.enums.EException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
