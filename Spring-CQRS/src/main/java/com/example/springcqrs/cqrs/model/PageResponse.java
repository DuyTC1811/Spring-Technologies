package com.example.springcqrs.cqrs.model;

import com.example.springcqrs.enums.CodeError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse<T> {
    private int code;
    private boolean success;
    private String message;
    private int limit;
    private int offset;
    private int totalPage;
    private T data;

    public PageResponse(T data) {
        this.data = setData(data);
    }

    public PageResponse(T data, CodeError code) {
        this.code = code.getCode();
        this.message = code.getName();
        this.success = true;
        this.data = setData(data);
    }

    public T setData(T data) {
        if (data == null) {
            this.code = CodeError.NO_DATA.getCode();
            this.message = CodeError.NO_DATA.getName();
            this.success = false;
        }
        return data;
    }
}
