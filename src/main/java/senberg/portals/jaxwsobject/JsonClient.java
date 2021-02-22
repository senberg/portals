package senberg.portals.jaxwsobject;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class JsonClient {
    public static void main(String[] args) {
        URI uri = UriBuilder.fromUri("http://localhost/").build();
        ClientConfig config = new ClientConfig();
        javax.ws.rs.client.Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(uri);

        JsonCalculator.Result result = target.path("jsonCalculator").path("add").queryParam("a", "1").queryParam("b", "2").request().accept(MediaType.APPLICATION_JSON).get(JsonCalculator.Result.class);
        System.out.println(result.output);
    }

}
