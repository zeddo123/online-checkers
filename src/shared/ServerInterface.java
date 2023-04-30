package src.shared;

import src.server.GameMetaData;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public boolean registerPlayer(ClientIterface client, GameMetaData metaData) throws RemoteException;
    public boolean deregisterPlayer(ClientIterface client) throws RemoteException;
}
