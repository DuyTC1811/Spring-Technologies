package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.springrediscache.util.ConverterStringUntil.converterStringToObject;
import static com.example.springrediscache.util.ConverterStringUntil.converterToString;

@Service
@Transactional
public class RedisValueOperations implements RedisValueOperation {
    final Logger LOGGER = LoggerFactory.getLogger(RedisValueOperations.class);
    private final ValueOperations<String, String> valueOperations;

    public RedisValueOperations(final RedisTemplate<String, String> template) {
        this.valueOperations = template.opsForValue();
    }

    @Override
    public void create(final Customer customer) {
        String key = Customer.class.getSimpleName().toUpperCase();
        String data = converterToString(customer);
        valueOperations.setIfAbsent(key, data);
        LOGGER.info("Successful : Key {}, Data {}", key, data);
    }

    @Override
    public void createSetTime(int time, final Customer customer) {
        String key = Customer.class.getSimpleName().toUpperCase();
        String data = converterToString(customer);
        valueOperations.set(key, data, time, TimeUnit.MILLISECONDS);
        LOGGER.info("Successful : Key {}, Time {}, Data {}", key, time, data);
    }

    @Override
    public Customer getValue() {
        String key = Customer.class.getSimpleName().toUpperCase();
        Customer customer = converterStringToObject(valueOperations.get(key));
        if (Objects.isNull(customer)) return new Customer();
        LOGGER.info("Successful : Key {}, Data {}", key, customer);
        return customer;
    }

    @Override
    public void deleted() {
        String key = Customer.class.getSimpleName().toUpperCase();
        valueOperations.getOperations().delete(key);
        LOGGER.info("Successful : Key {}", key);
    }
}
