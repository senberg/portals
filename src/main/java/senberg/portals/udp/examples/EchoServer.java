package senberg.portals.udp.examples;

import senberg.portals.udp.WireServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends WireServer {

    public static void main(String[] args) throws SocketException {
        EchoServer echoServer = new EchoServer();
        echoServer.run();
    }

    private EchoServer() throws SocketException {
        super(666, 4096);
    }

    @Override
    protected void datagramReceived(DatagramPacket datagramPacket) {
        byte[] buffer = datagramPacket.getData();
        int length = datagramPacket.getLength();
        InetAddress sender = datagramPacket.getAddress();
        int port = datagramPacket.getPort();
        System.out.println("Received \"" + new String(buffer, 0, length) + "\" from " + sender + " port " + port);
        DatagramPacket response = new DatagramPacket(buffer, length, sender, port);

        try {
            send(response);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
