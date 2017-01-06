package com.jaxrs;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enums.ResponseEnum;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.model.User;
import com.response.ApiResponse;
import com.service.UserRoleService;
import com.service.UserService;
import com.util.JsonUtil;
import com.util.LogUtil;

@Path("/user")
public class UserAPI {

	UserService userService;
	UserRoleService userRoleService;

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@HeaderParam("authorization") String auth) {
		userService = new UserService();
		ApiResponse response = new ApiResponse();
		String result = "";
		LogUtil.log("User API :: finding all users", Level.INFO, null);

		try {
			response.setResponseData("");
			response.setResponseCode(ResponseEnum.USER_NOT_FOUND.getValue());
			response.setErrorMessage("Users not found.");

			List<User> users = userService.getAllUsers();

			if (users != null) {
				String[] userFieldNames = { "firstName", "lastName", "email",
						"type", "lastModified", "dob", "gender", "isDeleted" };
				FilterProvider filters = new SimpleFilterProvider().addFilter(
						"UserFilter", SimpleBeanPropertyFilter
								.filterOutAllExcept(userFieldNames));

				response.setResponseData(users);
				response.setResponseCode(ResponseEnum.SUCCESS.getValue());
				response.setErrorMessage("");
				result = JsonUtil.pojoToJSONWithFilters(response, filters);
			}

		} catch (Exception ex) {
			LogUtil.log("find users failed", Level.SEVERE, ex);
			response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
			response.setErrorMessage("Internal Server Error.");
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/createUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(@HeaderParam("authorization") String auth,
			String jsonString) {
		userService = new UserService();
		userRoleService = new UserRoleService();
		ApiResponse response = new ApiResponse();
		String result = "";
		LogUtil.log("User Service :: create user, payload is: " + jsonString,
				Level.INFO, null);

		try {
			response.setResponseData(null);
			response.setResponseCode(ResponseEnum.ERROR.getValue());
			response.setErrorMessage("User creation failed.");

			if (jsonString != null && !jsonString.isEmpty()) {
				User user = (User) JsonUtil.jsonToPOJO(jsonString, User.class);

				if (user != null) {

					try {
						if (user.getRoles().size() > 0) {
//							UserRole userRole = userRoleService.findByListOfIds(user.getroles))
//							user.getRoles().clear();
//							user.getRoles().add(e)
						}
						
						boolean isCreated = userService.createUser(user);
						if (isCreated) {
							response.setResponseData(user);
							response.setResponseCode(ResponseEnum.SUCCESS
									.getValue());
							response.setErrorMessage(null);
						}
					} catch (PersistenceException ex) {
						response.setResponseCode(ResponseEnum.EXCEPTION
								.getValue());
						response.setErrorMessage("User creation failed.");
					}
				} else {
					response.setErrorMessage("Insufficient Parametters...!");
				}
			} else {
				response.setErrorMessage("Insufficient Parametters...!");
			}

			String[] userFieldNames = { "firstName", "lastName", "email",
					"type", "lastModified", "dob", "gender", "isDeleted" };
			FilterProvider filters = new SimpleFilterProvider()
					.addFilter("UserFilter", SimpleBeanPropertyFilter
							.filterOutAllExcept(userFieldNames));
			result = JsonUtil.pojoToJSONWithFilters(response, filters);

		} catch (Exception ex) {
			response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
			response.setErrorMessage("Internal Server Error.");
			LogUtil.log("User Service :: User creation failed ", Level.SEVERE,
					null);
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

}