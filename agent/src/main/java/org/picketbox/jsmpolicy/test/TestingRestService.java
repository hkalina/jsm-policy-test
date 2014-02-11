package org.picketbox.jsmpolicy.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.StringWriter;
import java.lang.SecurityManager;
import java.security.AccessControlException;
import java.security.Policy;

// http://localhost:8080/JsmPolicyTestingAgent/rest/testing/securitymanager

@Path("/testing")
public class TestingRestService {
	
	@GET
	@Path("/securitymanager")
	public String getSecurityManager() {
		SecurityManager sm = System.getSecurityManager();
		return sm==null ? "null" : sm.getClass().getName();
	}
	
	@GET
	@Path("/policy")
	public String getPolicy() {
		Policy policy = Policy.getPolicy(); 
		return policy==null ? "null" : policy.getClass().getName();
	}
	
	@GET
	@Path("/policyfile")
	public String getPolicyFile() {
		return System.getProperty("java.security.policy");
	}
	
	@GET
	@Path("/test")
	public String getTest() {
		StringWriter sw = new StringWriter();
		sw.write("test10: /etc/passwd:\t");
		try{
			File f = new File("/etc/passwd");
			if(f.canWrite()) sw.write("WRITABLE");
			else if(f.canRead()) sw.write("READABLE");
			else sw.write("INACCESSIBLE");
		}
		catch(AccessControlException e){
			sw.write("DENIED");
		}
		catch(Exception e){
			sw.write("Exception "+e.toString());
		}
		sw.write("\n");
		
		return sw.toString();
	}
	
	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {
		String result = "Pozadavek: " + msg;
		System.err.println("pozadavaek");
		return Response.status(200).entity(result).build();
	}
	
}
