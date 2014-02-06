package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.util.Map;

import org.jboss.as.controller.client.helpers.domain.AddDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.DeploymentPlan;
import org.jboss.as.controller.client.helpers.domain.DeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.client.helpers.domain.DomainDeploymentManager;

public class DomainManagement extends Management {
	
	public DomainManagement(String host, int port) throws ManagementException {
		super(host, port);
	}
	
	public void deploy(String name, File file) throws ManagementException {
		
	}

	public String jsmPolicyOfServer(String server) throws ManagementException {
		return null;
	}

	public Map<String, String> jsmPoliciesOfServers() throws ManagementException {
		return null;
	}
	
	
}
