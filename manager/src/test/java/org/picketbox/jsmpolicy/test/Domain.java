package org.picketbox.jsmpolicy.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import com.sun.jersey.api.client.Client;

public class Domain {

    ModelControllerClient mcc; // JBoss Native API
    String profile;
    boolean domain;

    public Domain(String hostname, int port, String profile) throws IOException {
        mcc = ModelControllerClient.Factory.create(hostname, port);
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
        operation.get("policy").set(policy);
        String result = mcc.execute(operation).get(ClientConstants.OUTCOME).asString();
        return result.equals(ClientConstants.SUCCESS);
    }

    public boolean updateServer(String server, String policy) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.WRITE_ATTRIBUTE_OPERATION);
        subsystem(operation.get(ClientConstants.OP_ADDR)).add("server", server);
        operation.get(ClientConstants.NAME).set("policy");
        operation.get(ClientConstants.VALUE).set(policy);
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



}
