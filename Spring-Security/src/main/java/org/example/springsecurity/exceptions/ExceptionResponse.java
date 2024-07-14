package org.example.springsecurity.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResponse {
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy HH:MM:SS",
            timezone = "Asia/Ho_Chi_Minh"
    )
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer code;
    private String detail;
    private String description;

    public ExceptionResponse(int code) {
        this.code = code;
    }
    public ExceptionResponse(int code, String detail) {
        this.code = code;
        this.description = detail;
    }
    public ExceptionResponse(int code, String detail, String description) {
        this.code = code;
        this.detail = detail;
        this.description = description;
    }
}
