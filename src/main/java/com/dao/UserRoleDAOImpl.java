package com.dao;

import java.util.List;

import com.model.UserRole;

public class UserRoleDAOImpl extends GenericDAO implements UserRoleDAO{

	@Override
	public List<UserRole> loadAllUserRoles() {
		return findAll(UserRole.class);
	}

}
