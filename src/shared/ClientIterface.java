package src.shared;

import src.server.GameMetaData;
import src.server.Piece;
import src.server.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientIterface extends Remote {
    public void Deregistered() throws RemoteException;
    public void joinedSession(SessionInterface session) throws RemoteException;

    public void OnOpponentMove(List<Position> move)throws RemoteException;
    public void OnBoardChanged(List<Piece> yours, List<Piece> opponents)throws RemoteException;
    public void lostGame() throws RemoteException;
    public void wonGame() throws RemoteException;

}
