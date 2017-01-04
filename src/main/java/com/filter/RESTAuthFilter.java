package com.filter;

import com.jaxrs.BasicAuth;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import java.net.URI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class RESTAuthFilter implements ContainerRequestFilter {
	@Override
	public ContainerRequest filter(ContainerRequest containerRequest)
			throws WebApplicationException {
		boolean authencationRequired = true;
		String method = containerRequest.getMethod();
		if (method.equals("POST")) {
			URI url = containerRequest.getAbsolutePath();
			System.out.println("url: " + url);
			String[] urlArray = url.toString().split("/");
			String endPointName = urlArray[urlArray.length - 1];
			if (endPointName.equalsIgnoreCase("forgotPassword")
					|| endPointName.equalsIgnoreCase("populateMaps")) {
				authencationRequired = false;
			}
		}
		System.out.println("authencationRequired: " + authencationRequired);
		if (authencationRequired) {
			// Get the authentification passed in HTTP headers parameters
			String auth = containerRequest.getHeaderValue("authorization");

			// If the user does not have the right (does not provide any HTTP
			// Basic Auth)
			if (auth == null) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}

			// lap : loginAndPassword
			String[] credentials = BasicAuth.decode(auth);

			// If login or password fail
			if (credentials == null || credentials.length != 2) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}

			// Get credentials from database
			try {
				/*
				 * SubjectDAO subject = (SubjectDAO)
				 * InitialContext.doLookup(JNDI_SUB); HCP hcp =
				 * subject.findHcpByEmail(credentials[0]); Authenticate subject
				 * Boolean authentificationResult = new
				 * PasswordUtil().passwordCorrect(credentials[1],
				 * hcp.getPassword());
				 */
				Boolean authentificationResult = false;
				if (credentials[0].equalsIgnoreCase("irfan") && credentials[1].equalsIgnoreCase("password")) {
					authentificationResult = true;
				}

				// Refuse login and password
				if (!authentificationResult) {
					throw new WebApplicationException(Status.UNAUTHORIZED);
				}
			} catch (WebApplicationException e) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		}

		return containerRequest;
	}
}
