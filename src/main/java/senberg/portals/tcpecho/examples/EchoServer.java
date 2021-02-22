package senberg.portals.tcpecho.examples;

import senberg.portals.tcpecho.TCPConnection;
import senberg.portals.tcpecho.TCPEndpoint;

import java.io.IOException;
import java.util.function.Consumer;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        Consumer<TCPConnection> connectionHandler = connection -> {
            System.out.println("New connection accepted.");

            try {
                while(true) {
                    char c = (char)connection.inputStream.read();
                    System.out.print(c);
                    connection.outputStream.write(c);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
                connection.close();
            }
        };

        TCPEndpoint endpoint = new TCPEndpoint(1337, connectionHandler);
        Thread acceptThread = new Thread(endpoint, "Echo server endpoint.");
        System.out.println("Accepting new incoming connections ...");
        acceptThread.start();
    }
}
