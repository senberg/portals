package senberg.portals.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Calculator calculator = new Calculator();
        Remote remoteCalculator = UnicastRemoteObject.exportObject(calculator, 0);
        Registry registry = LocateRegistry.createRegistry(9999);
        registry.bind("Calculator", remoteCalculator);
    }
}
