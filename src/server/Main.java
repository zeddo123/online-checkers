package src.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Server server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("server", server);
        System.out.println("Server started!");
    }
}
