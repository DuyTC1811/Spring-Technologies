package com.example.springkafkastream.util;

import com.example.springkafkastream.entity.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterStringUntil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterStringUntil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String converterToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("converter Err {}", object);
            throw new RuntimeException(e);
        }
    }

    public static Customer converterStringToObject(String value) {
        try {
            return objectMapper.readValue(value, Customer.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Converter Err {}", value);
            return null;
        }
    }
}
