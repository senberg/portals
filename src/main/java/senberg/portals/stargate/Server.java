package senberg.portals.stargate;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        StargateServer stargateServer = new StargateServer(endpoint);
        stargateServer.addHandler(String.class, (object) -> System.out.println("String: " + object));
        stargateServer.addHandler(Integer.class, (object) -> System.out.println("Integer: " + object));
        stargateServer.start();

        while (true) {
            Thread.sleep(2000);
            stargateServer.broadcast("Hello from the server!");
            stargateServer.broadcast(456);
        }
    }
}
