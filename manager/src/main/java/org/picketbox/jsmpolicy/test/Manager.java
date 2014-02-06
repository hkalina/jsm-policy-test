package org.picketbox.jsmpolicy.test;

import java.util.Map.Entry;
import com.sun.jersey.api.client.Client;

class Manager {
	public static void main(String[] args){
		
		Client client = Client.create(); // REST client
		
		try {
			ServerConnection s = new ServerConnection("localhost", 9999);
			
			System.out.println(s.isStandalone()?"Standalone":"Domain");
			
			//s.getManipulator().deploy("JsmPolicyTestingAgent", new File("/home/honza/Wildfly/jsm-policy-test/agent/target/JsmPolicyTestingAgent.war"));
			
			for(Entry<String, String> entry : s.getManipulator().jsmPoliciesOfServers().entrySet()){
				System.out.println(entry.getKey()+"="+entry.getValue());
			}
			
			s.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("ok");
	}
}
