package senberg.portals.tcpecho;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPConnection {
    public final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;

    public TCPConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public TCPConnection(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
