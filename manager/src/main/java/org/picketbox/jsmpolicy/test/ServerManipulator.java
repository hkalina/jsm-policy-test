package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper.ServerDeploymentException;

public abstract class ServerManipulator {
	
	protected ServerConnection connection;
	
	public abstract String jsmPolicyOfServer(String server) throws IOException;
	
	public abstract Map<String, String> jsmPoliciesOfServers() throws IOException;
	
	public abstract void deploy(String name, File file) throws FileNotFoundException, ServerDeploymentException;
	
}
