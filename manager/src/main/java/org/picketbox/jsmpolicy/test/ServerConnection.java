package org.picketbox.jsmpolicy.test;

import java.io.IOException;
import java.net.InetAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class ServerConnection {
	
	private ModelControllerClient mcc;
	private boolean standalone;
	private ServerManipulator manipulator;
	
	public ServerConnection(String host, int port) throws IOException {
		InetAddress address = InetAddress.getByName(host);
		this.mcc = ModelControllerClient.Factory.create(address, port);
		this.standalone = isStandaloneInternal();
		
		if(standalone){
			manipulator = new ServerManipulatorStandalone(this);
		}else{
			manipulator = new ServerManipulatorDomain(this,"full");
		}
	}
	
	public boolean isStandalone() {
		return standalone;
	}
	
	public ServerManipulator getManipulator() {
		return manipulator;
	}
	
	public ModelControllerClient getController(){
		return mcc;
	}
	
	public void close() throws IOException {
		mcc.close();
	}
	
	private boolean isStandaloneInternal() throws IOException {
		ModelNode op = new ModelNode();
		op.get(ClientConstants.OP).set("read-attribute");
		op.get(ClientConstants.NAME).set("launch-type");
		String type = mcc.execute(op).get(ClientConstants.RESULT).asString();
		return type.equals("STANDALONE");
	}
	
}
