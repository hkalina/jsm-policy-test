package org.picketbox.jsmpolicy.test;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Map.Entry;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

class Manager {
	public static void main(String[] args){
		
		Client client = Client.create(); // REST client
		
		try {
			Management mc = new StandaloneManagement("localhost", 9999);
			System.out.println(mc.isStandalone()?"Standalone":"Domain");
			
			for(Entry<String, String> entry : mc.jsmPoliciesOfServers().entrySet()){
				System.out.println(entry.getKey()+"="+entry.getValue());
			}
			
			/*
			System.out.println("Deploying...");
			mc.deploy("JsmPolicyTestingAgent", new File("/home/honza/Wildfly/jsm-policy-test/agent/target/JsmPolicyTestingAgent.war"));
			System.out.println("Deployed");
			*/
			mc.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("ok");
	}
}
