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
 * Is called by Testing Manager to get information about environment of deployments
 */
@Path("/")
public class TestingRestService {

    /**
     * Test if is this testing REST service accessible (is deployed on tested server)
     *
     * http://localhost:8080/JsmPolicyTestingAgent/rest/accessible
     */
    @GET
    @Path("/accessible")
    public String isAccessible() {
        return "accessible";
    }

    /**
     * Get JBoss server node name
     *
     * http://localhost:8080/JsmPolicyTestingAgent/rest/accessible
     */
    @GET
    @Path("/nodename")
    public String getNodeName() {
        return System.getProperty("jboss.node.name");
    }

	/**
	 * Check if deployment's code can read given file
	 *
	 * http://localhost:8080/JsmPolicyTestingAgent/rest/readable?path=/etc/passwd
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
     * Try exit JVM - require permission:
     * java.lang.RuntimePermission "exitVM"
     *
     * http://localhost:8080/JsmPolicyTestingAgent/rest/exit
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

}
