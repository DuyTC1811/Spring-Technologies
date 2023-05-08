package com.example.springcqrs.repositorys;

import com.example.springcqrs.entitys.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAllByAge(PageRequest pageRequest, int age);

    Optional<Customer> findById(Integer integer);

    Optional<Customer> findByName(String name);
}
