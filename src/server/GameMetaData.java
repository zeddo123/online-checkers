package src.server;

import java.awt.*;
import java.io.Serializable;

public class GameMetaData implements Serializable {
    public Color color;
    public int gameid;

    public GameMetaData() {}
    public GameMetaData(Color color) {
        this.color = color;
    }
}
