package senberg.portals.jaxws;

import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
    public static void main(String[] args) throws MalformedURLException {
        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
    }

    public static int getSum(int a, int b) throws MalformedURLException {
        URL serviceUrl = new URL("http://localhost/calculator:10000?wsdl");
        QName serviceQName = new QName("http://jaxws.portals.senberg/", "CalculatorService");
        Service service = Service.create(serviceUrl, serviceQName);
        QName calculatorQName = new QName("http://jaxws.portals.senberg/", "CalculatorPort");
        RemoteCalculator remoteCalculator = service.getPort(calculatorQName, RemoteCalculator.class);

        return remoteCalculator.add(a, b);
    }
}
