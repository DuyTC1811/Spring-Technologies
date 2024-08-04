package org.example.springsecurity.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private int code;
    private String message;
    private String uuid;
    private T data;
}
