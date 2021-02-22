package senberg.portals.jaxrs;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Client {
    public static void main(String[] args) {
        URI uri = UriBuilder.fromUri("http://localhost/").build();
        ClientConfig config = new ClientConfig();
        javax.ws.rs.client.Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(uri);
        String plainAnswer = target.path("calculator").path("add").queryParam("a", "1").queryParam("b", "2").request().accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(plainAnswer);
    }
}
