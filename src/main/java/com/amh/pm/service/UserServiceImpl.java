package com.amh.pm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amh.pm.dao.UserDao;
import com.amh.pm.entity.User;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public void add(User user) {
		userDao.add(user);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

}
