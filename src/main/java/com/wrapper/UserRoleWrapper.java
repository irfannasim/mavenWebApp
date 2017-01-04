package com.wrapper;

import java.util.List;

public class UserRoleWrapper {

	private int id;
	private String name;
	private String description;
	private List<UserRoleWrapper> users;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UserRoleWrapper> getUsers() {
		return users;
	}

	public void setUsers(List<UserRoleWrapper> users) {
		this.users = users;
	}
}
