package com.dao;

import java.util.List;
import com.model.User;

public interface UserDAO {

	public List<User> getAllUsers();
	public boolean createUser(User user);
}