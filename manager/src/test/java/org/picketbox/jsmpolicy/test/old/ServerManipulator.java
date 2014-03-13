package org.picketbox.jsmpolicy.test.old;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public abstract class ServerManipulator {
	
	protected ServerConnection connection;
	
	protected abstract ModelNode subsystem(ModelNode node);
	
	public boolean isJsmPolicyPresent() throws IOException{
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set(ClientConstants.READ_CHILDREN_NAMES_OPERATION);
		subsystem(op.get(ClientConstants.OP_ADDR));
		op.get(ClientConstants.CHILD_TYPE).set("server");
		
		ModelNode out = connection.getController().execute(op);
		return out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS);
	}
	
	public String getJsmPolicyOfServer(String server) throws IOException {
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set(ClientConstants.READ_ATTRIBUTE_OPERATION);
		subsystem(op.get(ClientConstants.OP_ADDR)).add("server", server);
		op.get(ClientConstants.NAME).set("policy");
		
		ModelNode out = connection.getController().execute(op);
		if(!out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)){
			throw new RuntimeException("Reading attribute: "+out.get(ClientConstants.FAILURE_DESCRIPTION).asString());
		}
		String outString = out.get(ClientConstants.RESULT).asString();
		if(outString.equals("undefined")) return null;
		return outString;
	}
	
	public boolean isSetJsmPolicyOfServer(String server) throws IOException {
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set(ClientConstants.READ_ATTRIBUTE_OPERATION);
		subsystem(op.get(ClientConstants.OP_ADDR)).add("server", server);
		op.get(ClientConstants.NAME).set("policy");
		
		ModelNode out = connection.getController().execute(op);
		return out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS);
	}
	
	public void setJsmPolicyOfServer(String server, String value) throws IOException {
		ModelNode op = new ModelNode();
		subsystem(op.get(ClientConstants.OP_ADDR)).add("server", server);
		op.get(ClientConstants.NAME).set("policy");
		
		if(value==null){
			op.get(ClientConstants.OP).set(ClientConstants.UNDEFINE_ATTRIBUTE_OPERATION);
		}else{		
			op.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
			op.get(ClientConstants.VALUE).set(value);
		}
		
		ModelNode out = connection.getController().execute(op);
		if(!out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)){
			throw new RuntimeException("Writing attribute: "+out.get(ClientConstants.FAILURE_DESCRIPTION).asString());
		}
	}
	
	public void addJsmPolicyOfServer(String server, String value) throws IOException {
		ModelNode op = new ModelNode();
		subsystem(op.get(ClientConstants.OP_ADDR)).add("server", server);
		op.get(ClientConstants.OP).set(ClientConstants.ADD);
		op.get("policy").set(value);
		
		ModelNode out = connection.getController().execute(op);
		System.out.println(out.toJSONString(false));
		
		if(!out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)){
			throw new RuntimeException("Adding attribute: "+out.get(ClientConstants.FAILURE_DESCRIPTION).asString());
		}
	}
	
	public void removeJsmPolicyOfServer(String server) throws IOException {
		ModelNode op = new ModelNode();
		subsystem(op.get(ClientConstants.OP_ADDR)).add("server", server);
		op.get(ClientConstants.OP).set(ClientConstants.REMOVE_OPERATION);
		
		ModelNode out = connection.getController().execute(op);
		System.out.println(out.toJSONString(false));
		
		if(!out.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)){
			throw new RuntimeException("Removing node: "+out.get(ClientConstants.FAILURE_DESCRIPTION).asString());
		}
	}
	
	public Map<String, String> jsmPoliciesOfServers() throws IOException {
		ModelNode result;
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set(ClientConstants.READ_CHILDREN_NAMES_OPERATION);
		subsystem(op.get(ClientConstants.OP_ADDR));
		op.get(ClientConstants.CHILD_TYPE).set("server");
		
		result = connection.getController().execute(op).get(ClientConstants.RESULT);
		
		Map<String,String> out = new HashMap<String,String>();
		for(int i=0; result.has(i); i++){
			String name = result.get(i).asString();
			out.put(name, getJsmPolicyOfServer(name));
		}
		return out;
	}
	
}
