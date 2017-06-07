package com.amh.pm.dao;

import java.util.List;

import com.amh.pm.entity.User;

public interface UserDao {

    void add(User user);

    List<User> findAll();

    User findById(int id);
}
