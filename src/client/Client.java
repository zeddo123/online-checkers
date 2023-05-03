package src.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import src.server.*;
import src.shared.*;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client implements ClientIterface {
    public ServerInterface remoteServer;
    public SessionInterface runningSession;
    public CheckersBoard board = new CheckersBoard(this);
    public Label turnLabel = new Label("Game hasn't started yet!");
    public GameMetaData metaData;
    public boolean registered = false;

    public Client() throws RemoteException, NotBoundException {
        UnicastRemoteObject.exportObject(this, 0);
        var localhost = LocateRegistry.getRegistry("localhost", 1099);
        remoteServer = (ServerInterface) localhost.lookup("server");
    }

    public void registerToPlay(GameMetaData metaData) {
        this.metaData = metaData;
        try {
            remoteServer.registerPlayer(this, metaData);
            registered = true;
            System.out.println("Registered to play!");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void Deregister() {
        if (!registered) {
            try {
                UnicastRemoteObject.unexportObject(this, false);
            } catch (NoSuchObjectException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        try {
            remoteServer.deregisterPlayer(this);
        } catch (RemoteException e) {
            System.out.println("Couldn't send server.deregisterPlayer(this)");
        }
    }

    public List<List<Position>> getPieceMoves(Piece p) {
        try {
            return runningSession.getPossiblePieceMoves(this, p);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public MoveError playMove(List<Position> moves) {
        try {
            return runningSession.makeMove(this, moves);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void Deregistered() {
        System.out.println("Got deregistered from server");
        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("Unexported!");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinedSession(SessionInterface session) {
        System.out.println("Joined Game session!");
        this.runningSession = session;

        var client = this;
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (session.isTurn(client)){
                            turnLabel.setText("Your turn to Play");
                        } else {
                            turnLabel.setText("Waiting for opponent to Play");
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            this.board.yourColor = this.metaData.color;
            this.board.opponentsColor = session.getOpponentColor(this);
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
        var client = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (runningSession.isTurn(client)){
                        turnLabel.setText("Your turn to Play");
                    } else {
                        turnLabel.setText("Waiting for opponent to Play");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                board.OnBoardChanged(yours, opponents);
            }
        });
    }

    @Override
    public void lostGame() {
        Platform.runLater(() -> {
            UI.showAlert("GAME OVER!\nYou lost :(");
        });

    }

    @Override
    public void wonGame() {
        Platform.runLater(() -> {
            UI.showAlert("GAME OVER!\nYou won :)");
        });

    }

}
