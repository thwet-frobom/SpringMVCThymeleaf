package com.amh.pm.service;

import java.util.List;

import com.amh.pm.entity.User;

public interface UserService {

	void add(User user);
	
	List<User> findAll();
}
