package senberg.portals.websocketmap;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint(value = "/calculator/")
public class CalculatorServerEndpoint {
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private final Calculator calculator = new Calculator();
    private final ByteBuffer responseBuffer = ByteBuffer.allocate(16);

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        System.out.println("Socket Connected: " + sess);
    }

    @OnMessage
    public void onWebSocketData(Session session, ByteBuffer data) throws IOException {
        int type = data.getInt(0);

        if (type == 0) {
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Thanks"));
        } else {
            long requestId = data.getLong(4);
            int a = data.getInt(12);
            int b = data.getInt(16);

            int result = calculator.add(a, b);

            responseBuffer.position(0);
            responseBuffer.putInt(0, 1);
            responseBuffer.putLong(4, requestId);
            responseBuffer.putInt(12, result);
            session.getBasicRemote().sendBinary(responseBuffer);
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket Closed: " + reason);
        closureLatch.countDown();
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }
}