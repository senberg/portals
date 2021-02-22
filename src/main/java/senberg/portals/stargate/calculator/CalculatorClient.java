package senberg.portals.stargate.calculator;

import senberg.portals.stargate.StargateClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CalculatorClient {
    static BlockingQueue<Integer> receivedCalculatorResponses = new ArrayBlockingQueue<>(10);
    static StargateClient stargateClient;

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        stargateClient = new StargateClient(endpoint);
        stargateClient.addHandler(CalculatorResponse.class, (response) -> {
            CalculatorResponse calculatorResponse = (CalculatorResponse) response;
            receivedCalculatorResponses.add(calculatorResponse.result);
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
        stargateClient.send(new CalculatorAddRequest(a, b));
        return receivedCalculatorResponses.take();
    }
}
