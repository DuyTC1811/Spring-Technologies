package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class RedisHashOperations implements RedisHashOperation {
    final Logger LOGGER = LoggerFactory.getLogger(RedisValueOperations.class);
    private final HashOperations<String, Integer, Customer> hashOperations;

    public RedisHashOperations(final RedisTemplate<String, String> template) {
        this.hashOperations = template.opsForHash();
    }

    @Override
    public void create(Customer customer) {
        String key = Customer.class.getSimpleName().toUpperCase();
        hashOperations.put(key, customer.getId(), customer);
        LOGGER.info(String.format("Customer with ID %s saved", customer.getId()));
    }

    @Override
    public void update(Customer customer) {
        String key = Customer.class.getSimpleName().toUpperCase();
        hashOperations.put(key, customer.getId(), customer);
        LOGGER.info(String.format("Customer with ID %s updated", customer.getId()));
    }

    @Override
    public Map<Integer, Customer> getAll() {
        String key = Customer.class.getSimpleName().toUpperCase();
        return hashOperations.entries(key);
    }

    @Override
    public void delete(Integer id) {
        String key = Customer.class.getSimpleName().toUpperCase();
        hashOperations.delete(key, id);
        LOGGER.info(String.format("Customer with ID %s deleted", id));
    }
}
