package senberg.portals.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        long start = System.currentTimeMillis();
        long total = 0;

        for (int i = 0; i < 5000; i++) {
            total += getSum(i, i + 1);
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + total);
        System.out.println("Completed in " + duration + " ms.");
    }

    public static int getSum(int a, int b) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 9999);
        RemoteCalculator remoteCalculator = (RemoteCalculator) registry.lookup("Calculator");
        return remoteCalculator.add(a, b);
    }
}
