package com.example.dynamicdatasource.services;

import com.example.dynamicdatasource.models.User;

import java.util.List;

public interface IUserService {
    List<User> getListMaster();
    List<User> getListSlave();
}
