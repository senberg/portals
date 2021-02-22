package senberg.portals.rmi;

import java.rmi.RemoteException;

public class Calculator implements RemoteCalculator {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
