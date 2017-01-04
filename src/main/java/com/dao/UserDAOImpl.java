package com.dao;

import java.util.List;
import com.model.User;

public class UserDAOImpl extends GenericDAO implements UserDAO {

	@Override
	public List<User> getAllUsers() {
		return findAll(User.class);
	}

	@Override
	public boolean createUser(User user) {
		return save(user);
	}
}
