package senberg.portals.websocketmap;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class CalculatorClientEndpoint {
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private long requestCounter;
    private final Map<Long, Object> requestLocks = new HashMap<>();
    private final Map<Long, Integer> requestResponses = new HashMap<>();

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
            long requestId = data.getLong(4);
            int result = data.getInt(12);
            Object lock = requestLocks.get(requestId);

            synchronized (lock) {
                requestResponses.put(requestId, result);
                requestLocks.get(requestId).notifyAll();
            }
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
        long requestId = requestCounter++;
        requestBuffer.putLong(4, requestId);
        Object lock = new Object();
        requestLocks.put(requestId, lock);

        synchronized (lock) {
            session.getBasicRemote().sendBinary(requestBuffer);
            lock.wait();
            requestLocks.remove(requestId);
            return requestResponses.remove(requestId);
        }
    }
}