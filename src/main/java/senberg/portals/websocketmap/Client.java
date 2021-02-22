package senberg.portals.websocketmap;

import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class Client {
    static Session session;
    static CalculatorClientEndpoint clientEndpoint = new CalculatorClientEndpoint();
    static ByteBuffer requestBuffer = ByteBuffer.allocate(20);

    public static void main(String[] args) {
        URI uri = URI.create("ws://localhost:8080/calculator/");

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            try {
                // Create client side endpoint

                // Attempt Connect
                session = container.connectToServer(clientEndpoint, uri);

                long start = System.currentTimeMillis();
                long total = 0;

                for(int i=0; i<5000; i++) {
                    total += getSum(i, i+1);
                }

                long duration = System.currentTimeMillis() - start;
                System.out.println("Total: " + total);
                System.out.println("Completed in " + duration + " ms.");

                // Send another message
                ByteBuffer goodbyeBuffer = ByteBuffer.allocate(4);
                goodbyeBuffer.putInt(0, 0);
                session.getBasicRemote().sendBinary(goodbyeBuffer);

                // Wait for remote to close
                clientEndpoint.awaitClosure();

                // Close session
                session.close();
            } finally {
                // Force lifecycle stop when done with container.
                // This is to free up threads and resources that the
                // JSR-356 container allocates. But unfortunately
                // the JSR-356 spec does not handle lifecycles (yet)
                LifeCycle.stop(container);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private static long getSum(int a, int b) throws IOException, InterruptedException {
        requestBuffer.position(0);
        requestBuffer.putInt(0, 1);
        requestBuffer.putInt(12, a);
        requestBuffer.putInt(16, b);
        return clientEndpoint.sendRequest(session, requestBuffer);
    }
}