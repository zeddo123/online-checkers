package src.client;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        System.out.println("Client Side");
        var ui = new UI();
        ui.show();
    }
}
