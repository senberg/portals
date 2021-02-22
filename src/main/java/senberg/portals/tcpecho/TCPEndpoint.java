package senberg.portals.tcpecho;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class TCPEndpoint implements Runnable {
    private final ServerSocket serverSocket;
    private final Consumer<TCPConnection> newClientHandler;

    public TCPEndpoint(int port, Consumer<TCPConnection> newClientHandler) throws IOException {
        this.newClientHandler = newClientHandler;
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.setReceiveBufferSize(65536);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        serverSocket.bind(inetSocketAddress, 64);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                TCPConnection connection = new TCPConnection(clientSocket);
                newClientHandler.accept(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
