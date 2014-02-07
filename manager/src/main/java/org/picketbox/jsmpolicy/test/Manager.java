package org.picketbox.jsmpolicy.test;

import com.sun.jersey.api.client.Client;

class Manager {
	public static void main(String[] args){
		
		Client client = Client.create(); // REST client
		
		try {
			ServerConnection s = new ServerConnection("localhost", 9999);
			System.out.println(s.isStandalone()?"Standalone":"Domain");
			System.out.println(s.getManipulator().isJsmPolicyPresent()?"Present":"NOT PRESENT!");
			
			
			
			
			/*
			for(Entry<String, String> entry : s.getManipulator().jsmPoliciesOfServers().entrySet()){
				System.out.println(entry.getKey()+"="+entry.getValue());
			}
			*/
			
			s.close();
			System.out.println("ok");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
