package senberg.portals.stargate;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        StargateClient stargateClient = new StargateClient(endpoint);
        stargateClient.addHandler(String.class, (object) -> System.out.println("String: " + object));
        stargateClient.addHandler(Integer.class, (object) -> System.out.println("Integer: " + object));
        stargateClient.start();

        while (true) {
            Thread.sleep(1000);
            stargateClient.send("Hello from the client!");
            stargateClient.send(123);
        }
    }
}
