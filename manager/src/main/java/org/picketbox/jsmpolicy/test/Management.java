package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

/**
 * JBoss AS Native Management API client
 */
abstract class Management {
	
	protected ModelControllerClient mcc;
	protected InetAddress host;
	protected int port;
	
	public Management(String host, int port) throws ManagementException {
		try {
			this.host = InetAddress.getByName(host);
			this.port = port;
			mcc = ModelControllerClient.Factory.create(host, port);
		}
		catch (UnknownHostException e) {
			throw new ManagementException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isStandalone() throws IOException {
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-attribute");
		//ModelNode address = op.get(ClientConstants.OP_ADDR);
		//address.add("subsystem", "modcluster");
		op.get(ClientConstants.NAME).set("launch-type");
		return mcc.execute(op).get(ClientConstants.RESULT).asString().equals("STANDALONE");
	}
	
	public abstract String jsmPolicyOfServer(String server) throws ManagementException;
	
	public abstract Map<String, String> jsmPoliciesOfServers() throws ManagementException;
	
	//abstract public void deploy(String name, File file) throws ManagementException;
	
	class ManagementException extends Exception {
		private static final long serialVersionUID = 4251889893385299179L;
		public ManagementException(String message){
			super(message);
		}
	}
	
	public void close(){
		try{
			mcc.close();
		}
		catch(IOException e){}
	}
}
