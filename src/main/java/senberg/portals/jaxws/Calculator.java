package senberg.portals.jaxws;

import jakarta.jws.WebService;

@WebService(endpointInterface = "senberg.portals.jaxws.RemoteCalculator")
public class Calculator implements RemoteCalculator{
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
