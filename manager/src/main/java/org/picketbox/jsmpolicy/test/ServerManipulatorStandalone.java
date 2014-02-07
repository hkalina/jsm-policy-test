package org.picketbox.jsmpolicy.test;

import org.jboss.dmr.ModelNode;

public class ServerManipulatorStandalone extends ServerManipulator {

	public ServerManipulatorStandalone(ServerConnection connection) {
		this.connection = connection;
	}
	
	protected ModelNode subsystem(ModelNode node) {
		return node.add("subsystem", "jsmpolicy");
	}
	
	/*
	// s.getManipulator().deploy("JsmPolicyTestingAgent", new File("/home/honza/Wildfly/jsm-policy-test/agent/target/JsmPolicyTestingAgent.war"));
	public void deploy(String name, File file) throws FileNotFoundException, ServerDeploymentException {
		ServerDeploymentHelper deployer = new ServerDeploymentHelper(connection.getController());
		if(file.canRead()) System.out.println("canRead");
		FileInputStream stream = new FileInputStream(file);
		String ret = deployer.deploy(name, stream);
	}
	*/
	
}
