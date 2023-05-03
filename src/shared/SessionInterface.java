package src.shared;

import src.server.Piece;
import src.server.Position;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SessionInterface extends Remote {
    ClientIterface getOpponent(ClientIterface self) throws RemoteException;

    boolean isTurn(ClientIterface self) throws RemoteException;

    List<List<Position>> getPossibleMoves(ClientIterface self) throws RemoteException;

    List<List<Position>> getPossiblePieceMoves(ClientIterface self, Piece p) throws RemoteException;

    MoveError makeMove(ClientIterface self, List<Position> move) throws RemoteException;

    Color getOpponentColor(ClientIterface client) throws RemoteException;
}
