package com.jaxrs;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enums.ResponseEnum;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@HeaderParam("authorization") String auth,
			@PathParam("userId") int userId) {
		userService = new UserService();
		ApiResponse response = new ApiResponse();
		String result = "";
		LogUtil.log("User API :: finding user by Id", Level.INFO, null);

		try {
			response.setResponseData("");
			response.setResponseCode(ResponseEnum.USER_NOT_FOUND.getValue());
			response.setErrorMessage("User not found.");

			User user = userService.findById(userId);

			if (user != null) {
				String[] userFieldNames = { "id", "firstName", "lastName",
						"email", "type", "lastModified", "dob", "gender",
						"isDeleted" };
				FilterProvider filters = new SimpleFilterProvider().addFilter(
						"UserFilter", SimpleBeanPropertyFilter
								.filterOutAllExcept(userFieldNames));

				response.setResponseData(user);
				response.setResponseCode(ResponseEnum.SUCCESS.getValue());
				response.setErrorMessage("");
				result = JsonUtil.pojoToJSONWithFilters(response, filters);
			}

		} catch (Exception ex) {
			LogUtil.log("find user by id failed", Level.SEVERE, ex);
			response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
			response.setErrorMessage("Internal Server Error.");
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

			String[] userFieldNames = { "id", "firstName", "lastName", "email",
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
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updateUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@HeaderParam("authorization") String auth,
			String jsonString) {
		userService = new UserService();
		ApiResponse response = new ApiResponse();
		String result = "";
		LogUtil.log("User Service :: Update user, payload is: " + jsonString,
				Level.INFO, null);

		try {
			response.setResponseData(null);
			response.setResponseCode(ResponseEnum.ERROR.getValue());
			response.setErrorMessage("User update failed.");

			if (jsonString != null && !jsonString.isEmpty()) {
				User user = (User) JsonUtil.jsonToPOJO(jsonString, User.class);

				if (user != null) {

					try {
						boolean isUpdated = userService.updateUser(user);
						if (isUpdated) {
							response.setResponseData(user);
							response.setResponseCode(ResponseEnum.SUCCESS
									.getValue());
							response.setErrorMessage(null);
						}
					} catch (PersistenceException ex) {
						response.setResponseCode(ResponseEnum.EXCEPTION
								.getValue());
						response.setErrorMessage("User update failed.");
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
			LogUtil.log("User Service :: User update failed ", Level.SEVERE,
					null);
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@DELETE
	@Path("/deleteUser/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(@HeaderParam("authorization") String auth,
			@PathParam("userId") int userId) {
		userService = new UserService();
		ApiResponse response = new ApiResponse();
		String result = "";
		LogUtil.log("User Service :: delete user by id:: " + userId,
				Level.INFO, null);

		try {
			response.setResponseData(null);
			response.setResponseCode(ResponseEnum.ERROR.getValue());
			response.setErrorMessage("User deletion failed.");

			User user = userService.findById(userId);

			if (user != null) {

				try {

					boolean isDeleted = userService.deleteUser(user);
					if (isDeleted) {
						response.setResponseData(null);
						response.setResponseCode(ResponseEnum.SUCCESS
								.getValue());
						response.setErrorMessage(null);
					}
				} catch (PersistenceException ex) {
					response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
					response.setErrorMessage("User deletion failed.");
				}
			} else {
				response.setResponseCode(ResponseEnum.ERROR.getValue());
				response.setErrorMessage("User not found...!");
			}
			result = JsonUtil.pojoToJSONwithoutFilters(response);

		} catch (JsonGenerationException | JsonMappingException ex) {
			response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
			response.setErrorMessage("Converting java object to JSON failed.");
			LogUtil.log("User Service :: User deletion failed ", Level.SEVERE,
					null);
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
			response.setErrorMessage("Internal Server Error.");
			LogUtil.log("User Service :: User deletion failed ", Level.SEVERE,
					null);
			try {
				result = JsonUtil.pojoToJSONwithoutFilters(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
}