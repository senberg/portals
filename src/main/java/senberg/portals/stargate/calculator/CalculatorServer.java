package senberg.portals.stargate.calculator;

import senberg.portals.stargate.StargateServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Calculator calculator = new Calculator();
        InetSocketAddress endpoint = new InetSocketAddress("localhost", 22222);
        StargateServer stargateServer = new StargateServer(endpoint);
        stargateServer.addHandler(CalculatorAddRequest.class, (object) -> {
            CalculatorAddRequest calculatorAddRequest = (CalculatorAddRequest) object;
            int result = calculator.add(calculatorAddRequest.a, calculatorAddRequest.b);
            stargateServer.broadcast(new CalculatorResponse(result));
        });
        stargateServer.start();
    }
}
