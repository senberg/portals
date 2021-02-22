package senberg.portals.websocketqueue;

import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class Client {
    static Session session;
    static CalculatorClientEndpoint clientEndpoint = new CalculatorClientEndpoint();
    static ByteBuffer requestBuffer = ByteBuffer.allocate(12);

    public static void main(String[] args) throws IOException, DeploymentException, InterruptedException {
        URI uri = URI.create("ws://localhost:8080/calculator/");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(clientEndpoint, uri);

        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");

        ByteBuffer goodbyeBuffer = ByteBuffer.allocate(4);
        goodbyeBuffer.putInt(0, 0);
        session.getBasicRemote().sendBinary(goodbyeBuffer);
        clientEndpoint.awaitClosure();
        session.close();
    }

    private static long getSum(int a, int b) throws IOException, InterruptedException {
        requestBuffer.position(0);
        requestBuffer.putInt(0, 1);
        requestBuffer.putInt(4, a);
        requestBuffer.putInt(8, b);
        return clientEndpoint.sendRequest(session, requestBuffer);
    }
}