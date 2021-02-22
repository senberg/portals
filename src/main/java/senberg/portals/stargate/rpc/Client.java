package senberg.portals.stargate.rpc;

import senberg.portals.stargate.StargateClient;
import senberg.portals.stargate.calculator.CalculatorAddRequest;
import senberg.portals.stargate.calculator.CalculatorResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Client {
    static BlockingQueue<Object> receivedResponses = new ArrayBlockingQueue<>(10);
    static StargateClient stargateClient;

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        stargateClient = new StargateClient(endpoint);
        stargateClient.addHandler(RPCResponse.class, (response) -> {
            RPCResponse rpcResponse = (RPCResponse) response;
            receivedResponses.add(rpcResponse.object);
        });
        stargateClient.start();
        Thread.sleep(1000);

        long start = System.currentTimeMillis();
        long total = 0;

        for(int i=0; i<5000; i++) {
            total += getSum(i, i+1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
        stargateClient.close();
    }

    public static int getSum(int a, int b) throws IOException, InterruptedException {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.objectName = "calculator";
        rpcRequest.methodName = "add";
        rpcRequest.parameters = new ArrayList<Object>();
        rpcRequest.parameters.add(a);
        rpcRequest.parameters.add(b);
        stargateClient.send(rpcRequest);
        Object response = receivedResponses.take();
        return (int) response;
    }
}
