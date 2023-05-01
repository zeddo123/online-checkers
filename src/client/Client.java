package src.client;

import src.server.GameMetaData;
import src.server.Piece;
import src.server.Position;
import src.server.Session;
import src.shared.ClientIterface;
import src.shared.ServerInterface;
import src.shared.SessionInterface;

import java.awt.*;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
        try {
            UnicastRemoteObject.unexportObject(this, false);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinedSession(SessionInterface session) {
        System.out.println("Joined Game session!");
        try {
            System.out.println(session.getPossibleMoves(this));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void OnOpponentMove(List<Position> move) {
        System.out.println(move);
    }

    @Override
    public void OnBoardChanged(List<Piece> yours, List<Piece> opponents) {
        System.out.println("The Board Changed");
    }

    @Override
    public void lostGame() {
        System.out.println("lost game");

    }

    @Override
    public void wonGame() {
        System.out.println("won game");

    }
}
