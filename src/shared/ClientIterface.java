package src.shared;

import src.server.GameMetaData;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientIterface extends Remote {
    public void Deregistered() throws RemoteException;
    public void joinedSession(ClientIterface otherClient, GameMetaData otherMetaData) throws RemoteException;
    public void lostGame() throws RemoteException;
    public void wonGame() throws RemoteException;

}
