package com.service;

import java.util.List;

import com.dao.UserDAO;
import com.dao.UserDAOImpl;
import com.model.User;

public class UserService {

	UserDAO userDAO;

	public UserService() {
		userDAO = new UserDAOImpl();
	}

	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	public boolean createUser(User user) {
		userDAO = new UserDAOImpl();
		return userDAO.createUser(user);
	}

}
