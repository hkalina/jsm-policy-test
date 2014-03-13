package org.picketbox.jsmpolicy.test.old;

import org.jboss.dmr.ModelNode;

public class ServerManipulatorDomain extends ServerManipulator {
	
	private String profile;
	
	public ServerManipulatorDomain(ServerConnection connection, String profile) {
		this.connection = connection;
		this.profile = profile;
	}
	
	protected ModelNode subsystem(ModelNode node) {
		return node.add("profile", profile).add("subsystem", "jsmpolicy");
	}
	
}
