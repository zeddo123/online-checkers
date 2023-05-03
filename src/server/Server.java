package src.server;

import src.client.Client;
import src.shared.ClientIterface;
import src.shared.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {
    private List<User> waitingPool = new ArrayList<>();
    private List<Session> runningGames = new ArrayList<>();

    public Server() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public boolean registerPlayer(ClientIterface client, GameMetaData metaData) {
        waitingPool.add(new User(client, metaData));
        System.out.println("New Player added to waiting pool");
        startNewSession();
        return true;
    }

    private boolean removeFromWaitingPool(ClientIterface client) {
        for(var user : waitingPool) {
            if(user.equals(client)) {
                System.out.println("User removed from waiting pool");
                waitingPool.remove(user);
                return true;
            }
        }
        return false;
    }

    private boolean removeFromSession(ClientIterface client) {
        System.out.println("Trying to remove from session");
        for(var session : runningGames){
            var pair = session.pair.contains(client);
            if (pair == null)
                continue;
            var victim = pair.user2;
            try {
                victim.client.Deregistered();
            } catch (RemoteException e) {
                System.out.println("Couldn't deregister victim");
            }
            runningGames.remove(session);
            System.out.println("User removed from Session; and the Session itself.");
            return true;
        }
        return false;
    }

    @Override
    public boolean deregisterPlayer(ClientIterface client) {
        if(removeFromWaitingPool(client) || removeFromSession(client)){
            try {
                client.Deregistered();
            } catch (RemoteException e) {
                System.out.println("Couldn't deregister client");
            }
            System.out.println("Player disconnected!");
            return true;
        }
        return false;
    }

    public Session startNewSession() {
        if (waitingPool.size() < 2) {
            return null;
        }
        var user1 = waitingPool.get(0);
        var user2 = waitingPool.get(1);

        waitingPool.remove(user1);
        waitingPool.remove(user2);

        Session session = null;
        try {
            session = new Session(user1, user2);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        runningGames.add(session);

        // Broadcast new Session to concerned users here
        try {
            user1.client.joinedSession(session);
            user2.client.joinedSession(session);
            user1.client.OnBoardChanged(session.getGame().currPlayerPieces(user1.metaData.gameid), session.getGame().currPlayerPieces(user2.metaData.gameid));
            user2.client.OnBoardChanged(session.getGame().currPlayerPieces(user2.metaData.gameid), session.getGame().currPlayerPieces(user1.metaData.gameid));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return session;
    }
}
