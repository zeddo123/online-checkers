package src.server;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session {
    private CheckerGame game;
    public Pair pair = new Pair();

    public Session(User user1, User user2) {
        user1.metaData.gameid = 1;
        user2.metaData.gameid = -1;
        pair.user1 = user1;
        pair.user2 = user2;
    }

    public CheckerGame getGame() {
        return game;
    }
}
