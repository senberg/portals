package senberg.portals.stargate;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StargateClient extends Thread {
    private final Map<Class<?>, Consumer<Object>> objectHandlers = new HashMap<>();
    private final SocketAddress endpoint;
    private ObjectOutputStream objectOutputStream;
    Socket socket;
    private volatile boolean stop = false;

    public StargateClient(SocketAddress endpoint) {
        this.endpoint = endpoint;
    }

    public void addHandler(Class<?> c, Consumer<Object> consumer) {
        objectHandlers.put(c, consumer);
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                socket = new Socket();
                socket.setReuseAddress(true);
                socket.connect(endpoint);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                while (!stop) {
                    Object object = objectInputStream.readObject();
                    Class<?> c = object.getClass();
                    Consumer<Object> consumer = objectHandlers.get(c);

                    if (consumer != null) {
                        consumer.accept(object);
                    } else {
                        System.err.println("Unhandled object: " + c);
                    }
                }
            } catch (SocketException | EOFException e) {
                // Server disconnected
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                objectOutputStream = null;
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void send(Object object) throws IOException {
        if (objectOutputStream != null) {
            try {
                objectOutputStream.writeObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        stop = true;

        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
