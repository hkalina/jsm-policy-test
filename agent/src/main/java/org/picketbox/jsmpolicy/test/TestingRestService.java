package org.picketbox.jsmpolicy.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.lang.SecurityManager;
import java.security.Policy;

// http://localhost:8080/RESTfulExample/rest/testing/

@Path("/testing")
public class TestingRestService {
	
	@GET
	@Path("/securitymanager")
	public String getSecurityManager() {
		return System.getSecurityManager().getClass().getName();
	}
	
	@GET
	@Path("/policy")
	public String getPolicy() {
		return Policy.getPolicy().getClass().getName();
	}
	
	@GET
	@Path("/policyfile")
	public String getPolicyFile() {
		return System.getProperty("java.security.policy");
	}
	
	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {
		String result = "Pozadavek: " + msg;
		System.err.println("pozadavaek");
		return Response.status(200).entity(result).build();
	}
	
}
