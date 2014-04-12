package org.picketbox.jsmpolicy.test;

import java.io.IOException;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class Domain {

    ModelControllerClient mcc; // JBoss Native API
    String profile;
    boolean domain;

    public Domain(String protocol, String hostname, int port, String profile) throws IOException {
        mcc = ModelControllerClient.Factory.create(protocol, hostname, port);
        this.profile = profile;
        this.domain = isDomain();
    }

    public void destroy() throws IOException {
        mcc.close();
    }

    private boolean isDomain() throws IOException {
        ModelNode op = new ModelNode();
        op.get(ClientConstants.OP).set("read-attribute");
        op.get(ClientConstants.NAME).set("launch-type");
        ModelNode result = mcc.execute(op).get(ClientConstants.RESULT);
        return result.asString().equals("DOMAIN");
    }

    public ModelNode subsystem(ModelNode node) {
        if(domain) node.add("profile", profile);
        return node.add("subsystem", "jsmpolicy");
    }

    public boolean addPolicy(String policy, String file) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.ADD);
        subsystem(operation.get(ClientConstants.OP_ADDR)).add("policy", policy);
        operation.get("file").set(file);
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean updatePolicy(String policy, String file) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
        subsystem(operation.get(ClientConstants.OP_ADDR)).add("policy", policy);
        operation.get(ClientConstants.NAME).set("file");
        operation.get(ClientConstants.VALUE).set(file);
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean addOrUpdatePolicy(String policy, String file) throws IOException {
        if(updatePolicy(policy, file)) return true;
        if(addPolicy(policy, file)) return true;
        return false;
    }

    public boolean addServer(String server, String policy) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.ADD);
        subsystem(operation.get(ClientConstants.OP_ADDR)).add("server", server);
        if(policy!=null) operation.get("policy").set(policy);
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean updateServer(String server, String policy) throws IOException {
        ModelNode operation = new ModelNode();
        subsystem(operation.get(ClientConstants.OP_ADDR)).add("server", server);
        operation.get(ClientConstants.NAME).set("policy");
        if(policy==null){
            operation.get(ClientConstants.OP).set(ClientConstants.UNDEFINE_ATTRIBUTE_OPERATION);
        }else{
            operation.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
            operation.get(ClientConstants.VALUE).set(policy);
        }
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean addOrUpdateServer(String server, String policy) throws IOException {
        if(updateServer(server, policy)) return true;
        if(addServer(server, policy)) return true;
        return false;
    }

    public boolean setServerAndPolicy(String server, String policy, String file) throws IOException {
        return addOrUpdatePolicy(policy, file) && addOrUpdateServer(server, policy);
    }

    public boolean removeSubsystem() throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.REMOVE_OPERATION);
        subsystem(operation.get(ClientConstants.OP_ADDR));
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean addSubsystem() throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.ADD);
        subsystem(operation.get(ClientConstants.OP_ADDR));
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean restartSubsystem() throws IOException {
        removeSubsystem();
        return addSubsystem();
    }

}
