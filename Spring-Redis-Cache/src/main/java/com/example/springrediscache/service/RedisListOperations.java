package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;
import com.example.springrediscache.entity.Range;
import com.example.springrediscache.util.ConverterStringUntil;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.springrediscache.util.ConverterStringUntil.converterStringToObject;
import static com.example.springrediscache.util.ConverterStringUntil.converterToString;

@Service
@Transactional
public class RedisListOperations implements RedisListOperation {
    private final ListOperations<String, String> listOperations;

    public RedisListOperations(final RedisTemplate<String, String> template) {
        this.listOperations = template.opsForList();
    }

    @Override
    public void create(List<Customer> listData) {
        String key = Customer.class.getSimpleName().toUpperCase();
        listData.forEach(customer -> listOperations.leftPush(key, converterToString(customer)));
    }

    @Override
    public List<Customer> listCustomer(Range range) {
        String key = Customer.class.getSimpleName().toUpperCase();
        final List<String> objects = listOperations.range(key, range.getTo(), range.getFrom());
        if (CollectionUtils.isEmpty(objects)) return Collections.emptyList();
        return objects.stream().map(ConverterStringUntil::converterStringToObject).collect(Collectors.toList());
    }

    @Override
    public Customer getId(Long index) {
        String key = Customer.class.getSimpleName().toUpperCase();
        String response = listOperations.index(key, index);
        if (Objects.isNull(response)) return new Customer();
        return converterStringToObject(response);
    }

    @Override
    public void delete(Customer customer, int count) {
        String key = Customer.class.getSimpleName().toUpperCase();
        listOperations.remove(key, count, ConverterStringUntil.converterToString(customer));
    }
}
