package senberg.portals.stargate.rpc;

import senberg.portals.stargate.StargateServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RPCServer {
    private final Map<String, Map<String, Function<List<Object>, Object>>> methods = new HashMap<>();

    public RPCServer(InetSocketAddress endpoint) {
        StargateServer stargateServer = new StargateServer(endpoint);
        stargateServer.addHandler(RPCRequest.class, (object) -> {
            RPCRequest request = (RPCRequest) object;
            String objectName = request.objectName;
            String methodName = request.methodName;
            Function<List<Object>, Object> method = methods.get(objectName).get(methodName);
            Object result = method.apply(request.parameters);
            stargateServer.broadcast(new RPCResponse(result));
        });
        stargateServer.start();
    }

    public void registerLocalObject(String name, Map<String, Function<List<Object>, Object>> methods) {
        this.methods.put(name, methods);
    }
}
