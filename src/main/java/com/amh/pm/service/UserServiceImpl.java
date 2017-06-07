package com.amh.pm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amh.pm.dao.UserDao;
import com.amh.pm.entity.User;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	@Transactional
	public void add(User user) {
		userDao.add(user);
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return userDao.findAll();
	}

}
