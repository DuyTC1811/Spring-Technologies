package com.example.springjwt.reprository;

import com.example.springjwt.entitys.ERole;
import com.example.springjwt.entitys.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.name =:name")
    Optional<Role> findByName(ERole name);
}
