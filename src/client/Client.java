package src.client;

import src.server.GameMetaData;
import src.shared.ClientIterface;
import src.shared.ServerInterface;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Client implements ClientIterface {
    public ServerInterface remoteServer;
    public Client() throws RemoteException, NotBoundException {
        UnicastRemoteObject.exportObject(this, 0);
        var localhost = LocateRegistry.getRegistry("localhost", 1099);
        remoteServer = (ServerInterface) localhost.lookup("server");

        remoteServer.registerPlayer(this, new GameMetaData(new Color(100, 200, 200)));
    }

    @Override
    public void Deregistered() {
        System.out.println("Got deregistered from server");
    }

    @Override
    public void joinedSession(ClientIterface otherClient, GameMetaData otherMetaData) {

    }

    @Override
    public void lostGame() {

    }

    @Override
    public void wonGame() {

    }
}
