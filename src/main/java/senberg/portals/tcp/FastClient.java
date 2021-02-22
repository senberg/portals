package senberg.portals.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class FastClient {
    static Socket socket;
    static ByteBuffer requestBuffer = ByteBuffer.allocate(8);
    static ByteBuffer responseBuffer = ByteBuffer.allocate(4);

    public static void main(String[] args) throws IOException, InterruptedException {
        socket = new Socket("localhost", 11111);
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

    public static int getSum(int a, int b) throws IOException {
        requestBuffer.putInt(0, a);
        requestBuffer.putInt(4, b);
        socket.getOutputStream().write(requestBuffer.array());
        socket.getInputStream().readNBytes(responseBuffer.array(), 0, 4);
        return responseBuffer.getInt(0);
    }
}
