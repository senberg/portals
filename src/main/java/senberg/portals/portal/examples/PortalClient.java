package senberg.portals.portal.examples;

import senberg.portals.portal.Portal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PortalClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Portal portal = new Portal(System.out::println);
        SocketAddress remote = new InetSocketAddress("localhost", 1337);
        portal.connect(remote);

        portal.outputStream.write('H');
        portal.outputStream.write('E');
        portal.outputStream.write('J');

        Thread.sleep(4000);
        portal.close();
    }
}
