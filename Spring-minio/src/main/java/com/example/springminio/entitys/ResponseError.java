package com.example.springminio.entitys;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NonNull
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseError {
    private String message;
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}