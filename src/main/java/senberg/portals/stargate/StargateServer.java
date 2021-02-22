package senberg.portals.stargate;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StargateServer extends Thread {
    private final Map<Class<?>, Consumer<Object>> objectHandlers = new HashMap<>();
    private final Set<ClientSocketHandler> clientSocketHandlers = new HashSet<>();
    private final InetSocketAddress endpoint;

    public StargateServer(InetSocketAddress endpoint) {
        this.endpoint = endpoint;
    }

    public void addHandler(Class<?> c, Consumer<Object> consumer) {
        objectHandlers.put(c, consumer);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(endpoint);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientSocketHandler clientSocketHandler = new ClientSocketHandler(clientSocket);
                    clientSocketHandler.start();
                    clientSocketHandlers.add(clientSocketHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientSocketHandler extends Thread {
        Socket clientSocket;
        ObjectOutputStream objectOutputStream;

        public ClientSocketHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
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
                // Client disconnected
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    objectOutputStream = null;
                    clientSocketHandlers.remove(this);
                    clientSocket.close();
                    clientSocket = null;
                } catch (IOException ignored) {
                }
            }
        }

        public void send(Object object) throws IOException {
            if (objectOutputStream != null) {
                objectOutputStream.writeObject(object);
            }
        }
    }

    public void broadcast(Object object) {
        for (ClientSocketHandler clientSocketHandler : clientSocketHandlers) {
            try {
                clientSocketHandler.send(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
