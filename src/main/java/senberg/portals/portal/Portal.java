package senberg.portals.portal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.function.Consumer;

public class Portal {
    private final Socket socket;
    private final Consumer<Object> listener;
    public OutputStream outputStream;

    public Portal(Consumer<Object> listener) throws SocketException {
        this.socket = new Socket();
        this.listener = listener;
        socket.setReceiveBufferSize(Integer.MAX_VALUE);
        socket.setSendBufferSize(Integer.MAX_VALUE);
        socket.setReuseAddress(true);
        socket.setKeepAlive(true);
    }

    public void connect(SocketAddress remote) throws IOException {
        socket.connect(remote);
        outputStream = socket.getOutputStream();

        (new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                int data;

                while(true){
                    data = inputStream.read();
                    System.out.println("Portal Listener read " + data);
                    listener.accept(data);
                }
            } catch (IOException ignored) {
                close();
            }
        }, "Portal Listener")).start();
    }

    public boolean connected() {
        return socket.isConnected();
    }

    public void close() {
        try {
            outputStream = null;
            socket.close();
        } catch(Exception ignored){

        }
    }
}
