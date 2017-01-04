package com.jaxrs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.persistence.PersistenceException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.enums.ResponseEnum;
import com.model.User;
import com.service.UserService;
import com.util.JsonUtil;
import com.util.LogUtil;
import com.wrapper.UserWrapper;

@Path("/user")
public class UserAPI {
	UserService userService;

	@GET
	@Path("/users")
	public Response getCountries(@HeaderParam("authorization") String auth) {
		userService = new UserService();
		Map<String, Object> returnDataMap = new LinkedHashMap<>();
		returnDataMap.put(ResponseEnum.STATUS.getValue(),
				ResponseEnum.ERROR.getValue());
		returnDataMap.put(ResponseEnum.REASON.getValue(),
				"Error no user found.");

		LogUtil.log("User Service :: finding all users", Level.INFO, null);

		List<User> users = userService.getAllUsers();

		if (users != null) {
			returnDataMap.put(ResponseEnum.STATUS.getValue(),
					ResponseEnum.SUCCESS.getValue());
			returnDataMap.put(ResponseEnum.REASON.getValue(),
					"No user found.");
			returnDataMap.put(ResponseEnum.DATA.getValue(), users);
		}
		String response = JsonUtil.toJson(returnDataMap);

		return Response.ok(response).build();
	}

	@POST
	@Path("/createUser")
	public Response createUser(@HeaderParam("authorization") String auth,
			String jsonString) {
		userService = new UserService();
		Map<String, String> returnDataMap = new LinkedHashMap<String, String>();
		returnDataMap.put(ResponseEnum.STATUS.name(),
				ResponseEnum.ERROR.getValue());
		LogUtil.log("User Service :: create user, payload is: " + jsonString,
				Level.INFO, null);

		try {
			if (jsonString != null && !jsonString.isEmpty()) {
				UserWrapper userWrapper = (UserWrapper) JsonUtil.fromJson(
						jsonString, UserWrapper.class);
				if (userWrapper != null) {
					User user = APIsUtil.buildUserObject(userWrapper);

					try {
						boolean isCreated = userService.createUser(user);
						if (isCreated) {
							returnDataMap.put(ResponseEnum.STATUS.name(),
									ResponseEnum.SUCCESS.getValue());
						}
					} catch (PersistenceException ex) {
						returnDataMap.put(ResponseEnum.REASON.name(),
								"Error while creating user...!");
					}

				} else {
					returnDataMap.put(ResponseEnum.REASON.name(),
							"Insufficient Parametters...!");
				}
			} else {
				returnDataMap.put(ResponseEnum.REASON.name(),
						"Insufficient Parametters...!");
			}
		} catch (Exception ex) {
			LogUtil.log("User Service :: User creation failed ", Level.SEVERE,
					null);
		}
		String response = JsonUtil.toJson(returnDataMap);

		return Response.ok(response).build();
	}

}