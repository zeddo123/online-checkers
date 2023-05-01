package src.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        var c = new Client();
        System.out.println("Client Side");

        //c.remoteServer.deregisterPlayer(c);
        //UnicastRemoteObject.unexportObject(c, false);
    }
}
