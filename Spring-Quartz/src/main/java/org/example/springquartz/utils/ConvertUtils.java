package org.example.springquartz.utils;

import lombok.experimental.UtilityClass;
import tools.jackson.databind.ObjectMapper;

@UtilityClass
public class ConvertUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static  <T> String objectToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException("CONVERT OBJECT TO JSON FAILED", ex);
        }
    }
}
