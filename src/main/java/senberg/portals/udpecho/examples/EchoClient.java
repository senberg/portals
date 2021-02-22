package senberg.portals.udpecho.examples;

import senberg.portals.udpecho.WireClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoClient extends WireClient {

    public static void main(String[] args) throws IOException {
        InetAddress serverAddress = InetAddress.getLocalHost();
        EchoClient echoClient = new EchoClient();
        Thread listenerThread = new Thread(echoClient);
        listenerThread.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        for (String input = bufferedReader.readLine(); input != null; input = bufferedReader.readLine()) {
            byte[] buffer = input.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, serverAddress, 666);
            echoClient.send(datagramPacket);
        }
    }

    private EchoClient() throws SocketException {
        super(4096);
    }

    @Override
    protected void datagramReceived(DatagramPacket datagramPacket) {
        byte[] buffer = datagramPacket.getData();
        int length = datagramPacket.getLength();
        InetAddress sender = datagramPacket.getAddress();
        int port = datagramPacket.getPort();
        System.out.println("Received \"" + new String(buffer, 0, length) + "\" from " + sender + " port " + port);
    }
}
