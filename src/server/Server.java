package src.server;

import src.shared.ClientIterface;
import src.shared.ServerInterface;

import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {
    private List<User> waitingPool = new ArrayList<>();
    private List<Session> runningGames = new ArrayList<>();

    @Override
    public boolean registerPlayer(ClientIterface client, GameMetaData metaData) {
        return waitingPool.add(new User(client, metaData));
    }

    public Session startNewSession() {
        if (waitingPool.size() < 2) {
            return null;
        }
        var user1 = waitingPool.get(0);
        var user2 = waitingPool.get(1);

        var session = new Session(user1, user2);
        runningGames.add(session);

        // TODO: Broadcast new Session to concerned users here

        return session;
    }
}
