package senberg.portals.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueClient {
    static Socket socket;
    static ByteBuffer requestBuffer = ByteBuffer.allocate(8);
    static ByteBuffer responseBuffer = ByteBuffer.allocate(4);
    static BlockingQueue<Integer> responses = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) throws IOException, InterruptedException {
        socket = new Socket("localhost", 11111);
        new Thread(() -> {
            try {
                while(true) {
                    socket.getInputStream().readNBytes(responseBuffer.array(), 0, 4);
                    responses.put(responseBuffer.getInt(0));
                }
            } catch (IOException | InterruptedException ignored) {
            }
        }).start();

        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
        socket.close();
    }

    public static int getSum(int a, int b) throws IOException, InterruptedException {
        requestBuffer.putInt(0, a);
        requestBuffer.putInt(4, b);
        socket.getOutputStream().write(requestBuffer.array());
        return responses.take();
    }
}
