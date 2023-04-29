package src.server;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session {
    private CheckerGame game;
    private final List<User> players = new ArrayList<>();

    public Session(User user1, User user2) {
        user1.metaData.gameid = 1;
        user2.metaData.gameid = -1;
        players.add(user1);
        players.add(user2);
    }

    public CheckerGame getGame() {
        return game;
    }
}
