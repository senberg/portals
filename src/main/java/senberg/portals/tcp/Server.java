package senberg.portals.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {
    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 11111);
        serverSocket.bind(inetSocketAddress);

        byte[] requestArray = new byte[8];
        ByteBuffer requestBuffer = ByteBuffer.wrap(requestArray);
        byte[] responseArray = new byte[4];
        ByteBuffer responseBuffer = ByteBuffer.wrap(responseArray);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            int offset = 0;

            while(true){
                int read = clientSocket.getInputStream().read(requestArray, offset, 8 - offset);

                if(read == -1){
                    clientSocket.close();
                    break;
                } else {
                    offset += read;

                    if(offset == 8){
                        int a = requestBuffer.getInt(0);
                        int b = requestBuffer.getInt(4);
                        int result = calculator.add(a, b);
                        responseBuffer.position(0);
                        responseBuffer.putInt(0, result);
                        clientSocket.getOutputStream().write(responseArray);
                        offset = 0;
                    }
                }
            }
        }
    }
}
