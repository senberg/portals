package senberg.portals.jaxws;

import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class FastClient {
    static URL serviceUrl;
    static QName serviceQName;
    static Service service;
    static QName calculatorQName;
    static RemoteCalculator remoteCalculator;

    public static void main(String[] args) throws MalformedURLException {
        serviceUrl = new URL("http://localhost/calculator:10000?wsdl");
        serviceQName = new QName("http://jaxws.portals.senberg/", "CalculatorService");
        service = Service.create(serviceUrl, serviceQName);
        calculatorQName = new QName("http://jaxws.portals.senberg/", "CalculatorPort");
        remoteCalculator = service.getPort(calculatorQName, RemoteCalculator.class);

        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
    }

    public static int getSum(int a, int b) {
        return remoteCalculator.add(a, b);
    }
}
