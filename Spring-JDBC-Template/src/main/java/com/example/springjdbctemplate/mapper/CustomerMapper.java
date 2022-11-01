package com.example.springjdbctemplate.mapper;

import com.example.springjdbctemplate.entitys.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Customer.builder()
                .id(resultSet.getInt("id"))
                .contactNo(resultSet.getString("first_name"))
                .country(resultSet.getString("last_name"))
                .dob(resultSet.getString("email"))
                .email(resultSet.getString("gender"))
                .firstName(resultSet.getString("contact_no"))
                .gender(resultSet.getString("country"))
                .lastName(resultSet.getString("dob"))
                .build();
    }
}
