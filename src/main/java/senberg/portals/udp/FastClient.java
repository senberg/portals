package senberg.portals.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class FastClient {
    static DatagramSocket socket;
    static InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 7777);
    static ByteBuffer requestBuffer = ByteBuffer.allocate(8);
    static ByteBuffer responseBuffer = ByteBuffer.allocate(4);
    static DatagramPacket requestPacket = new DatagramPacket(requestBuffer.array(), 0, requestBuffer.array().length, inetSocketAddress);
    static DatagramPacket responsePacket = new DatagramPacket(responseBuffer.array(), 0, responseBuffer.array().length);

    public static void main(String[] args) throws IOException, InterruptedException {
        socket = new DatagramSocket();
        long start = System.currentTimeMillis();
        long total = 0;

        for(int i=0; i<5000; i++) {
            total += getSum(i, i+1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
        socket.close();
    }

    public static int getSum(int a, int b) throws IOException {
        requestBuffer.putInt(0, a);
        requestBuffer.putInt(4, b);
        socket.send(requestPacket);
        socket.receive(responsePacket);
        return responseBuffer.getInt(0);
    }
}
