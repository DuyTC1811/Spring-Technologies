package com.example.springsso.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidExceptionResponse {
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy HH:MM:SS",
            timezone = "Asia/Ho_Chi_Minh"
    )
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer code;
    private String detail;
    private Map<String, Object> description;

    public ValidExceptionResponse(int code, String detail, Map<String, Object> description) {
        this.code = code;
        this.detail = detail;
        this.description = description;
    }
}
