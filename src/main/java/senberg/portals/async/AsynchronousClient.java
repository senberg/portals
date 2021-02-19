package senberg.portals.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousClient {
    private final AsynchronousSocketChannel channel;
    private final ByteBuffer readBuffer;

    public AsynchronousClient() throws IOException {
        channel = AsynchronousChannelProvider.provider().openAsynchronousSocketChannel(null);
        channel.setOption(StandardSocketOptions.SO_SNDBUF, Integer.MAX_VALUE);
        channel.setOption(StandardSocketOptions.SO_RCVBUF, Integer.MAX_VALUE);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        readBuffer = ByteBuffer.allocate(Integer.MAX_VALUE);
    }

    public boolean isConnected() {
        try {
            channel.getRemoteAddress();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void connect(InetSocketAddress remote) throws Throwable {
        try {
            Future<Void> result = channel.connect(remote);
            result.get();
        }
        catch (ExecutionException executionException){
            throw executionException.getCause();
        }
    }

    public void read() {
        channel.read(readBuffer, null, new CompletionHandler<>() {

            @Override
            public void completed(Integer result, Object attachment) {

            }

            @Override
            public void failed(Throwable t, Object attachment) {
                t.printStackTrace();
            }
        });
    }
}
