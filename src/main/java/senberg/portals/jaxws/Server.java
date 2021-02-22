package senberg.portals.jaxws;

import jakarta.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost/calculator:10000", new Calculator());
    }
}
