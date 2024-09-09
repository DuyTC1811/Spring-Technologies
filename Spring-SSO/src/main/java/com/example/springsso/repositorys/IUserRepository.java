package com.example.springsso.repositorys;

import com.example.springsso.entitys.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
}
