package senberg.portals.udpecho;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

abstract public class WireClient implements Runnable{
    private final DatagramSocket datagramSocket;
    private final int bufferSize;
    private final byte[] buffer;

    public WireClient(int bufferSize) throws SocketException {
        datagramSocket = new DatagramSocket();
        this.bufferSize = bufferSize;
        buffer = new byte[bufferSize];
    }

    public void run() {
        try {
            while (true) {
                final DatagramPacket datagramPacket = new DatagramPacket(buffer, bufferSize);
                datagramSocket.receive(datagramPacket);
                datagramReceived(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract protected void datagramReceived(DatagramPacket datagramPacket);

    protected void send(DatagramPacket datagramPacket) throws IOException {
        datagramSocket.send(datagramPacket);
    }
}
