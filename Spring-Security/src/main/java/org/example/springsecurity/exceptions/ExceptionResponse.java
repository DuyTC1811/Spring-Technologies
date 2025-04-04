package org.example.springsecurity.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public record ExceptionResponse(
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy HH:mm:ss",
                timezone = "Asia/Ho_Chi_Minh"
        )
        LocalDateTime timestamp,
        Integer code,
        String detail,
        String description
) {
    public ExceptionResponse(int code, String detail, String description) {
        this(LocalDateTime.now(), code, detail, description);
    }
}
