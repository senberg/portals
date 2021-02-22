package senberg.portals.stargate.rpc;

import senberg.portals.stargate.StargateServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        RPCServer rpcServer = new RPCServer(endpoint);

        Calculator calculator = new Calculator();
        Map<String, Function<List<Object>, Object>> methods = new HashMap<>();
        methods.put("add", (list) -> {
            int a = (Integer) list.get(0);
            int b = (Integer) list.get(1);
            return calculator.add(a, b);
        });
        rpcServer.registerLocalObject("calculator", methods);
    }
}
