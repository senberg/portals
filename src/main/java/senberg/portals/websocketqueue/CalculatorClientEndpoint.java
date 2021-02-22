package senberg.portals.websocketqueue;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class CalculatorClientEndpoint {
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    BlockingQueue<Integer> responseQueue = new ArrayBlockingQueue<>(10);

    @OnOpen
    public void onWebSocketConnect(Session session) {
        System.out.println("Socket Connected: " + session);
    }

    @OnMessage
    public void onWebSocketText(Session session, ByteBuffer data) throws IOException {
        int type = data.getInt(0);

        if (type == 0) {
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Thanks"));
        } else {
            int result = data.getInt(4);
            responseQueue.add(result);
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

    public int sendRequest(Session session, ByteBuffer requestBuffer) throws IOException, InterruptedException {
        session.getBasicRemote().sendBinary(requestBuffer);
        return responseQueue.take();
    }
}