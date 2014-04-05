package org.picketbox.jsmpolicy.test;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Server {

    Client client = Client.create(); // REST client
    String url;

    public Server(String url) {
        this.url = url;
    }

    public void destroy() {
        client.destroy();
    }

    public boolean isAccessible() {
        WebResource wr = client.resource(url + "rest/testing/accessible");
        String response = wr.get(String.class);
        return response.equals("accessible");
    }

    public boolean isReadable(String pathOfFile) {
        WebResource wr = client.resource(url + "rest/testing/readable");

        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
        queryParams.add("path", pathOfFile);
        wr = wr.queryParams(queryParams);

        return wr.get(String.class).equals("true");
    }

}
