package senberg.portals.jaxrs;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class FastClient {
    static URI uri = UriBuilder.fromUri("http://localhost/").build();
    static ClientConfig config = new ClientConfig();
    static javax.ws.rs.client.Client client = ClientBuilder.newClient(config);
    static WebTarget target = client.target(uri).path("calculator").path("add");

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
    }

    public static int getSum(int a, int b) {
        return target.queryParam("a", a).queryParam("b", b).request().accept(MediaType.TEXT_PLAIN).get(Integer.class);
    }
}
