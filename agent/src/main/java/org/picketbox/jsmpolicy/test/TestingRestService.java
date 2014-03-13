package org.picketbox.jsmpolicy.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.SecurityManager;
import java.security.AccessControlException;
import java.security.Policy;

/**
 * Testing REST service
 * Is called by Testing manager to get information about deployments environment
 *
 * http://localhost:8080/JsmPolicyTestingAgent/rest/testing/accessible
 */
@Path("/testing")
public class TestingRestService {

    /**
     * Test if is this testing REST service accessible (is deployed on tested server)
     */
    @GET
    @Path("/accessible")
    public String isAccessible() {
        return "accessible";
    }

    /**
     * Get class name of security manager used on this server
     */
	@GET
	@Path("/securitymanager")
	public String getSecurityManager() {
		SecurityManager sm = System.getSecurityManager();
		return sm==null ? "null" : sm.getClass().getName();
	}

	/**
	 * Get class name of security policy used on this server
	 */
	@GET
	@Path("/policy")
	public String getPolicy() {
		Policy policy = Policy.getPolicy();
		return policy==null ? "null" : policy.getClass().getName();
	}

	/**
	 * Get class name of security policy file used on this server
	 */
	@GET
	@Path("/policyfile")
	public String getPolicyFile() {
		return System.getProperty("java.security.policy");
	}

	/**
	 * Get java home directory
	 */
	@GET
	@Path("/javahome")
	public String getJavaHome() {
		return System.getProperty("java.home");
	}

	/**
     * Get JBoss home directory
     */
	@GET
	@Path("/jbosshomedir")
	public String getJbossHomeDir() {
		return System.getProperty("jboss.home.dir");
	}

	/**
     * Try exit JVM
     *
     * This should be restricted by permission:
     * java.lang.RuntimePermission "exitVM"
     */
	@GET
	@Path("/exit")
	public String exitVM() {
		try{
			System.exit(123);
		}
		catch(AccessControlException e){
			return e.getMessage();
		}
		return "exited";
	}

	/**
	 * Try get first line from file /etc/passwd
	 *
	 * This should be restricted by permission:
	 * java.io.FilePermission "file:/etc/passwd"
	 */
	@GET
	@Path("/passwd")
	public String getTest() {
		StringWriter sw = new StringWriter();
		sw.write("test10: /etc/passwd:\t");
		try{
			File f = new File("/etc/passwd");
			if(f.canWrite()) sw.write("WRITABLE");
			else if(f.canRead()) sw.write("READABLE");
			else sw.write("INACCESSIBLE");

			sw.write("\n");
			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			sw.write(b.readLine());
			b.close();
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

	/**
	 * Check if deployment's code can read given file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/readable")
	public String fileReadable(@QueryParam("path") String path) throws IOException {
	    File file = new File(path);
	    try{
            FileInputStream is = new FileInputStream(file);
            is.read();
            is.close();
            return "true";
        }
        catch(AccessControlException e){
            return "false";
        }
	}

	/**
	 * Debug
	 */
	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {
		String result = "Unknown request: " + msg;
		return Response.status(200).entity(result).build();
	}

}
