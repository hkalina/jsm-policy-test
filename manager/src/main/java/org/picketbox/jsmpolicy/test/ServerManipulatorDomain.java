package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper.ServerDeploymentException;
import org.jboss.dmr.ModelNode;

public class ServerManipulatorDomain extends ServerManipulator {
	
	private String profile;
	
	public ServerManipulatorDomain(ServerConnection connection, String profile) {
		this.connection = connection;
		this.profile = profile;
	}
	
	public String jsmPolicyOfServer(String server) throws IOException {
		String out;
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-attribute");
		ModelNode address = op.get(ClientConstants.OP_ADDR);
		address.add("profile", profile);
		address.add("subsystem", "jsmpolicy");
		address.add("server", server);
		op.get(ClientConstants.NAME).set("policy");
		
		out = connection.getController().execute(op).get(ClientConstants.RESULT).asString();
		if(out.equals("undefined")) return null;
		return out;
	}
	
	public Map<String, String> jsmPoliciesOfServers() throws IOException {
		ModelNode result;
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-children-names");
		ModelNode address = op.get(ClientConstants.OP_ADDR);
		address.add("profile", profile);
		address.add("subsystem", "jsmpolicy");
		op.get(ClientConstants.CHILD_TYPE).set("server");
		
		result = connection.getController().execute(op).get(ClientConstants.RESULT);
		
		Map<String,String> out = new HashMap<String,String>();
		for(int i=0; result.has(i); i++){
			String name = result.get(i).asString();
			out.put(name, jsmPolicyOfServer(name));
		}
		return out;
	}
	
	public void deploy(String name, File file) throws FileNotFoundException, ServerDeploymentException {
		// TODO
	}

}
