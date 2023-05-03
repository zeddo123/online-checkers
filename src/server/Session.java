package src.server;

import src.shared.*;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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

    public User getOpponentUser(ClientIterface self) {
        return pair.contains(self).user2;
    }

    public boolean notifyEndOfGame() {
        if(game.isTheGameOver()){
            var winner = game.gameover == pair.user1.metaData.gameid ? pair.user1.client : pair.user2.client;
            var loser = game.gameover != pair.user1.metaData.gameid ? pair.user1.client : pair.user2.client;
            try {
                System.out.println("Ending Game");
                winner.wonGame();
                loser.lostGame();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean isTurn(ClientIterface self) {
        var p = pair.contains(self);
        var user = p.user1;
        //notifyEndOfGame();
        return game.turn == user.metaData.gameid;
    }

    @Override
    public List<List<Position>> getPossibleMoves(ClientIterface self) {
        var user = pair.contains(self).user1;
        return GraphMoves.AllPaths(game.allPossibleMovesOf(user.metaData.gameid));
    }

    @Override
    public List<List<Position>> getPossiblePieceMoves(ClientIterface self, Piece p) {
        var user = pair.contains(self).user1;
        return GraphMoves.AllPaths(game.possibleMoves(user.metaData.gameid, p, user.metaData.gameid, true, null, false));
    }

    @Override
    public MoveError makeMove(ClientIterface self, List<Position> move) {
        var p = pair.contains(self);
        var user = p.user1;
        var opp = p.user2;
        try {
            game.makeMove(user.metaData.gameid, GraphMoves.toGraph(move));
            opp.client.OnBoardChanged(game.currPlayerPieces(opp.metaData.gameid), game.currPlayerPieces(user.metaData.gameid));
            user.client.OnBoardChanged(game.currPlayerPieces(user.metaData.gameid), game.currPlayerPieces(opp.metaData.gameid));
            return null;
        } catch (RemoteException e) {
            System.out.println("Couldn't access remote object!");
            return MoveError.ServerSideError;
        } catch (UncompleteMoveException e) {
            return MoveError.UnCompleteMove;
        } catch (NotTurnException e) {
            return MoveError.NotTurn;
        } catch (TooManyMoveOptions e) {
            return MoveError.TooManyMoveOptions;
        } catch (IllegalMoveException e) {
            return MoveError.IllegalMove;
        } catch (GameOverException e) {
            notifyEndOfGame();
            return MoveError.GameOver;
        } catch (PlayersPieceNotFoundException e) {
            return MoveError.PlayerPieceNotFound;
        }
    }

    @Override
    public Color getOpponentColor(ClientIterface client) throws RemoteException {
        return getOpponentUser(client).metaData.color;
    }
}
