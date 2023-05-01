package src.server;

import src.shared.ClientIterface;
import src.shared.SessionInterface;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session implements SessionInterface {
    private CheckerGame game = new CheckerGame();
    public Pair pair = new Pair();

    public Session(User user1, User user2) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        user1.metaData.gameid = 1;
        user2.metaData.gameid = -1;
        pair.user1 = user1;
        pair.user2 = user2;
    }

    public CheckerGame getGame() {
        return game;
    }

    @Override
    public ClientIterface getOpponent(ClientIterface self) {
        return pair.contains(self).user2.client;
    }

    @Override
    public boolean isTurn(ClientIterface self) {
        var p = pair.contains(self);
        var user = p.user1;
        var opp = p.user2;
        if(game.isTheGameOver()){
            if(game.gameover == user.metaData.gameid){
                try {
                    user.client.wonGame();
                    opp.client.lostGame();
                } catch (RemoteException e) {
                    //throw new RuntimeException(e);
                }
            } else {
                try {
                    opp.client.wonGame();
                    user.client.lostGame();
                } catch (RemoteException e) {

                }
            }
        }
        return game.turn == pair.contains(self).user1.metaData.gameid;
    }

    @Override
    public List<List<Position>> getPossibleMoves(ClientIterface self) {
        game.CliBoard();
        var user = pair.contains(self).user1;
        System.out.println(game.allPossibleMovesOf(user.metaData.gameid));
        return GraphMoves.AllPaths(game.allPossibleMovesOf(user.metaData.gameid));
    }

    @Override
    public boolean makeMove(ClientIterface self, List<Position> move) {
        var p = pair.contains(self);
        var user = p.user1;
        var opp = p.user2;
        try {
            game.makeMove(user.metaData.gameid, GraphMoves.toGraph(move));
            opp.client.OnBoardChanged(game.currPlayerPieces(opp.metaData.gameid), game.currPlayerPieces(user.metaData.gameid));
            user.client.OnBoardChanged(game.currPlayerPieces(user.metaData.gameid), game.currPlayerPieces(opp.metaData.gameid));
            return true;
        } catch (NotTurnException | RemoteException e) {
            return false;
        } catch (TooManyMoveOptions e) {
            return false;
        } catch (PlayersPieceNotFoundException e) {
            return false;
        } catch (UncompleteMoveException e) {
            return false;
        } catch (IllegalMoveException e) {
            return false;
        } catch (GameOverException e) {
            return false;
        }
    }
}
