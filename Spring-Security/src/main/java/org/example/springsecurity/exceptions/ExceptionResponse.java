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
    private String message;

    public ExceptionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
