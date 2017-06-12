package com.amh.pm.service;

import java.util.List;

import com.amh.pm.entity.User;

public interface UserService {

	public void save(User user);

	public void delete(User user);

	public void update(User user);

	public List<User> findAll();

	public User findById(int userId);

	public User userByName(String name, String password);
	
	public List<User> findUserNameByOrgnId(int orgId);

	public User findUserIdByName(String userName);
}
