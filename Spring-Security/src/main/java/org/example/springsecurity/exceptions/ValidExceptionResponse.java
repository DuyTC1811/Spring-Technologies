package org.example.springsecurity.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidExceptionResponse(
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy HH:mm:ss",
                timezone = "Asia/Ho_Chi_Minh"
        )
        LocalDateTime timestamp,
        Integer code,
        String detail,
        Map<String, Object> description
) {
    public ValidExceptionResponse(int code, String detail, Map<String, Object> description) {
        this(LocalDateTime.now(), code, detail, description);
    }
}