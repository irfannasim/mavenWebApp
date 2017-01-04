package com.service;

import java.util.List;

import com.dao.UserRoleDAO;
import com.dao.UserRoleDAOImpl;
import com.model.UserRole;

public class UserRoleService {

	UserRoleDAO userRoleDAO;

	public UserRoleService() {
		userRoleDAO = new UserRoleDAOImpl();
	}

	public List<UserRole> getAllUsers() {
		return userRoleDAO.loadAllUserRoles();
	}
}
