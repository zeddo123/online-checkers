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
        System.out.println("New client" + client);
        waitingPool.add(new User(client, metaData));
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
        for(var session : runningGames){
            var pair = session.pair.contains(client);
            if (pair == null)
                continue;
            var victim = pair.user2;
            try {
                victim.client.Deregistered();
            } catch (RemoteException e) {
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

        var session = new Session(user1, user2);
        runningGames.add(session);

        // TODO: Broadcast new Session to concerned users here

        return session;
    }
}
