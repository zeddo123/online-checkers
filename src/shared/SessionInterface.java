package src.shared;

import src.server.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SessionInterface extends Remote {
    public ClientIterface getOpponent(ClientIterface self) throws RemoteException;
    public boolean isTurn(ClientIterface self) throws RemoteException;
    public List<List<Position>> getPossibleMoves(ClientIterface self) throws RemoteException;
    public boolean makeMove(ClientIterface self, List<Position> move) throws RemoteException;

}
