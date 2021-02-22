package senberg.portals.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client {
    static ByteBuffer requestBuffer = ByteBuffer.allocate(8);
    static ByteBuffer responseBuffer = ByteBuffer.allocate(4);

    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
    }

    public static int getSum(int a, int b) throws IOException {
        requestBuffer.putInt(0, a);
        requestBuffer.putInt(4, b);
        Socket socket = new Socket("localhost", 11111);
        socket.getOutputStream().write(requestBuffer.array());
        socket.getInputStream().readNBytes(responseBuffer.array(), 0, 4);
        socket.close();
        return responseBuffer.getInt(0);
    }
}
