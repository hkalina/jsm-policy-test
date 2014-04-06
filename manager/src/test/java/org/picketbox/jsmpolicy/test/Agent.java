package org.picketbox.jsmpolicy.test;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Agent {

    Client client = Client.create(); // REST client
    String url;

    public Agent(String url) {
        this.url = url;
    }

    public boolean isAccessible() {
        WebResource wr = client.resource(url + "rest/accessible");
        String response = wr.get(String.class);
        return response.equals("accessible");
    }

    public boolean isReadable(String pathOfFile) {
        WebResource wr = client.resource(url + "rest/readable");

        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
        queryParams.add("path", pathOfFile);
        wr = wr.queryParams(queryParams);

        return wr.get(String.class).equals("true");
    }

    public String getNodeName() {
        WebResource wr = client.resource(url + "rest/nodename");
        return wr.get(String.class);
    }

    public String getDomain(){
        String node = getNodeName();
        return node.substring(0, node.indexOf(':'));
    }

    public String getServer(){
        String node = getNodeName();
        return node.substring(node.indexOf(':')+1);
    }

    public void destroy() {
        client.destroy();
    }
}
