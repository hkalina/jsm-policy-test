package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper.ServerDeploymentException;
import org.jboss.dmr.ModelNode;

public class StandaloneManagement extends Management {
	
	public StandaloneManagement(String host, int port) throws ManagementException {
		super(host, port);
	}
	
	public String jsmPolicyOfServer(String server) throws ManagementException {
		String out;
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-attribute");
		ModelNode address = op.get(ClientConstants.OP_ADDR);
		address.add("subsystem", "jsmpolicy");
		address.add("server", server);
		op.get(ClientConstants.NAME).set("policy");
		try{
			out = mcc.execute(op).get(ClientConstants.RESULT).asString();
		}
		catch(IOException e){
			throw new ManagementException(e.getMessage());
		}
		if(out.equals("undefined")) return null;
		return out;
	}
	
	public Map<String, String> jsmPoliciesOfServers() throws ManagementException {
		ModelNode result;
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-children-names");
		ModelNode address = op.get(ClientConstants.OP_ADDR);
		address.add("subsystem", "jsmpolicy");
		op.get(ClientConstants.CHILD_TYPE).set("server");
		try{
			result = mcc.execute(op).get(ClientConstants.RESULT);
		}
		catch(IOException e){
			throw new ManagementException(e.getMessage());
		}
		Map<String,String> out = new HashMap<String,String>();
		for(int i=0; result.has(i); i++){
			String name = result.get(i).asString();
			out.put(name, jsmPolicyOfServer(name));
		}
		return out;
	}
	
	public void deploy(String name, File file) throws ManagementException {
		try {
			ServerDeploymentHelper deployer = new ServerDeploymentHelper(mcc);
			if(file.canRead()) System.out.println("canRead");
			FileInputStream stream = new FileInputStream(file);
			System.out.println("vystup: '"+deployer.deploy(name, stream)+"'");
			
		}
		catch (RuntimeException e) {
			System.out.println("RuntimeEx");
		}
		catch (ServerDeploymentException e) {
			System.out.println("DepEx");
			throw new ManagementException(e.getMessage());
		}
		catch (FileNotFoundException e) {
			throw new ManagementException("Deploying package was not found!");
		}
	}
	
}
