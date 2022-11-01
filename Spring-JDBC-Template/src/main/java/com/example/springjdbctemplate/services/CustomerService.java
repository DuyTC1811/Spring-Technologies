package com.example.springjdbctemplate.services;

import com.example.springjdbctemplate.entitys.Customer;
import com.example.springjdbctemplate.mapper.CustomerMapper;
import com.example.springjdbctemplate.repositorys.ICustomerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService implements ICustomerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final SimpleJdbcCall simpleJdbcCall;

    public CustomerService(JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("customer").usingGeneratedKeyColumns("id");
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("customer");
    }

    @Override
    public void save(Customer customer) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", customer.getId());
        parameters.put("first_name", customer.getFirstName());
        parameters.put("last_name", customer.getLastName());
        parameters.put("email", customer.getEmail());
        parameters.put("gender", customer.getGender());
        parameters.put("contact_no", customer.getContactNo());
        parameters.put("country", customer.getCountry());
        parameters.put("dob", customer.getDob());
        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public void update(Customer customer) {
        final String qsl = "UPDATE customer SET first_name = ? , last_name = ?, email = ?, gender = ?, contact_no = ? , country = ?, dob = ? WHERE id = ?";
        jdbcTemplate.update(qsl, customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getGender(), customer.getContactNo(), customer.getCountry(), customer.getDob(), customer.getId());
    }

    @Override
    public void delete(int customerId) {
        final String qsl = "DELETE FROM ONLY customer WHERE id =?";
        jdbcTemplate.update(qsl, customerId);
    }


    @Override
    public Customer getById(Integer customerId) {
        final String query = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new CustomerMapper(), customerId);
    }

    @Override
    public Map<String, Object> getPage(int page, int size) {
        Map<String, Object> response = new HashMap<>();
        String SQL_COUNT = "SELECT COUNT(*) FROM customer ";
        String SQL_ELEMENT = String.format("SELECT * FROM customer ORDER BY id DESC %s ", queryLimit(page, size));
        List<Customer> lisCustomer = jdbcTemplate.query(SQL_ELEMENT, new CustomerMapper());
        Integer totalElements = jdbcTemplate.queryForObject(SQL_COUNT, Integer.class);
        response.put("customers", lisCustomer);
        response.put("currentPage", page);
        response.put("totalItems", totalElements);
        response.put("totalPages", size);
        return response;
    }

    private String queryLimit(int offset, int limit) {
        StringBuilder limitBuilder = new StringBuilder();
        if (limit > 0) {
            limitBuilder
                    .append(" LIMIT ").append(limit)
                    .append(" OFFSET ").append(offset);
        }
        return limitBuilder.toString();
    }
}
