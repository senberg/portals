package senberg.portals.tcp.examples;

import senberg.portals.tcp.TCPConnection;
import senberg.portals.tcp.TCPEndpoint;

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
