package senberg.portals.tcpecho.examples;

import senberg.portals.tcpecho.TCPConnection;

import java.io.IOException;
import java.util.Random;

public class EchoClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Random random = new Random();
        System.out.println("Connecting to server ...");
        TCPConnection connection = new TCPConnection("localhost", 1337);

        while(true){
            System.out.print("Received: ");

            int available = connection.inputStream.available();
            for (int i = 0; i < available; i++) {
                char c = (char) connection.inputStream.read();
                System.out.print(c);
            }

            System.out.println();

            String message = Long.toString(random.nextLong());
            Thread.sleep(100);
            System.out.println("Sending: " + message);
            connection.outputStream.write(message.getBytes());
            connection.outputStream.write('\n');
        }
    }
}
