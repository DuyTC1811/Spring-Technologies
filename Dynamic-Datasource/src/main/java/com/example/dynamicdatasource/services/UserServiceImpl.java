package com.example.dynamicdatasource.services;

import com.example.dynamicdatasource.anotions.WithDatabase;
import com.example.dynamicdatasource.models.User;
import com.example.dynamicdatasource.repositorys.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.dynamicdatasource.configurations.DataSourceType.MASTER;
import static com.example.dynamicdatasource.configurations.DataSourceType.SALVE;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @WithDatabase(MASTER)
    public List<User> getListMaster() {
        return userRepository.findAll();
    }

    @Override
    @WithDatabase(SALVE)
    public List<User> getListSlave() {
        return userRepository.findAll();
    }
}
