package com.jaxrs;

import java.util.ArrayList;
import java.util.List;

import com.dao.UserRoleDAO;
import com.dao.UserRoleDAOImpl;
import com.model.User;
import com.model.UserRole;
import com.wrapper.UserWrapper;

public class APIsUtil {

	public static User buildUserObject(UserWrapper userWrapper) {
		User user = new User();
		UserRoleDAO userRoleDAO = new UserRoleDAOImpl();
		List<UserRole> userRoles = new ArrayList<UserRole>();

		user.setFirstName(userWrapper.getFirstName());
		user.setLastName(userWrapper.getLastName());
		user.setEmail(userWrapper.getEmail());
		user.setPassword(userWrapper.getPassword());
		user.setType(userWrapper.getType());
		user.setLastModified(System.currentTimeMillis());
		user.setDob(userWrapper.getDob());
		user.setGender(userWrapper.getGender());
		user.setDeleted(false);
		
		userRoles = userRoleDAO.loadAllUserRoles();
		user.setRoles(userRoles);
		
		return user;
	}
}
