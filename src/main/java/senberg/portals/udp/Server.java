package senberg.portals.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class Server {
    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 7777);
        DatagramSocket serverSocket = new DatagramSocket(inetSocketAddress);

        byte[] requestArray = new byte[8];
        ByteBuffer requestBuffer = ByteBuffer.wrap(requestArray);
        DatagramPacket requestPacket = new DatagramPacket(requestArray, requestArray.length);
        byte[] responseArray = new byte[4];
        ByteBuffer responseBuffer = ByteBuffer.wrap(responseArray);
        DatagramPacket responsePacket = new DatagramPacket(responseArray, responseArray.length);

        while (true) {
            serverSocket.receive(requestPacket);
            int a = requestBuffer.getInt(0);
            int b = requestBuffer.getInt(4);

            int result = calculator.add(a, b);

            responseBuffer.putInt(0, result);
            responsePacket.setAddress(requestPacket.getAddress());
            responsePacket.setPort(requestPacket.getPort());
            serverSocket.send(responsePacket);
        }
    }
}
