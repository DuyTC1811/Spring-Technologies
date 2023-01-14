package com.example.multidatabase.repositorys;

import com.example.multidatabase.entity.CustomerPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryPostgres extends JpaRepository<CustomerPostgres, Integer> {
    @Query("SELECT c FROM CustomerPostgres c WHERE c.id = :id")
    Optional<CustomerPostgres> findByIds(Integer id);
}
